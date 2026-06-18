package me.ciakid.game;

import me.ciakid.Plugin;
import me.ciakid.arena.WorldEditArenaService;
import me.ciakid.config.PluginConfiguration;
import me.ciakid.model.AnswerOption;
import me.ciakid.model.ArenaDefinition;
import me.ciakid.model.RoundQuestion;
import me.ciakid.player.QuizPlayerState;
import me.ciakid.question.QuestionFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class QuizGameService implements Listener {
    private final Plugin plugin;
    private final PluginConfiguration configuration;
    private final WorldEditArenaService arenaService;
    private final QuestionFactory questionFactory = new QuestionFactory();
    private final List<QuizGame> games = new ArrayList<>();

    public QuizGameService(Plugin plugin, PluginConfiguration configuration, WorldEditArenaService arenaService) {
        this.plugin = plugin;
        this.configuration = configuration;
        this.arenaService = arenaService;
    }

    public void join(Player player) {
        if (findGame(player.getUniqueId()).isPresent()) {
            player.sendMessage(Component.text("Ya estás dentro de una partida.", NamedTextColor.RED));
            return;
        }

        Optional<QuizGame> game = findWaitingGame().or(this::createGame);
        if (game.isEmpty()) {
            player.sendMessage(Component.text("No hay arenas disponibles en este momento.", NamedTextColor.RED));
            return;
        }

        addPlayer(game.get(), player);
    }

    public void leave(Player player) {
        Optional<QuizGame> game = findGame(player.getUniqueId());
        if (game.isEmpty()) {
            player.sendMessage(Component.text("No estás en una partida.", NamedTextColor.RED));
            return;
        }
        removePlayer(game.get(), player.getUniqueId(), true);
        player.sendMessage(Component.text("Has salido de la partida.", NamedTextColor.YELLOW));
    }

    public void shutdown() {
        List<QuizGame> activeGames = new ArrayList<>(games);
        activeGames.forEach(game -> finish(game, "El servidor está apagando el minijuego."));
    }

    public int activeGames() {
        return games.size();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        findGame(event.getPlayer().getUniqueId()).ifPresent(game -> removePlayer(game, event.getPlayer().getUniqueId(), false));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Optional<QuizGame> game = findGame(player.getUniqueId());
        if (game.isEmpty() || game.get().state() != QuizState.RUNNING) {
            return;
        }
        if (!arenaService.isBelow(game.get().arena().bounds(), player.getLocation())) {
            return;
        }
        eliminate(game.get(), player);
    }

    private Optional<QuizGame> createGame() {
        return configuration.arenas().stream()
                .filter(arena -> games.stream().noneMatch(game -> game.arena().id().equals(arena.id())))
                .min(Comparator.comparing(ArenaDefinition::id))
                .map(arena -> new QuizGame(
                        arena,
                        arenaService.capture(arena.bounds(), arena.gameSpawn().getWorld()),
                        questionFactory.createRounds(configuration.questions(), arena.floors(), arena.rounds())
                ))
                .map(game -> {
                    games.add(game);
                    return game;
                });
    }

    private Optional<QuizGame> findWaitingGame() {
        return games.stream()
                .filter(game -> game.state() == QuizState.WAITING)
                .filter(game -> !game.isFull())
                .findFirst();
    }

    private Optional<QuizGame> findGame(UUID uniqueId) {
        return games.stream().filter(game -> game.hasPlayer(uniqueId)).findFirst();
    }

    private void addPlayer(QuizGame game, Player player) {
        game.addPlayer(player);
        player.getInventory().clear();
        player.teleport(game.arena().waitingSpawn());
        broadcast(game, Component.text(player.getName() + " entró al quiz (" + game.playerCount() + "/" + game.arena().maxPlayers() + ").", NamedTextColor.GREEN));

        if (game.playerCount() >= game.arena().minPlayers()) {
            startCountdown(game);
        }
    }

    private void startCountdown(QuizGame game) {
        if (game.state() != QuizState.WAITING) {
            return;
        }
        game.state(QuizState.COUNTDOWN);
        runTimer(game, configuration.countdownSeconds(), secondsLeft -> {
            broadcast(game, Component.text("La partida empieza en " + secondsLeft + "s.", NamedTextColor.GOLD));
            if (secondsLeft == 0) {
                startGame(game);
            }
        });
    }

    private void startGame(QuizGame game) {
        game.state(QuizState.RUNNING);
        game.players().forEach(state -> onlinePlayer(state.uniqueId()).ifPresent(player -> {
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(game.arena().gameSpawn());
        }));
        startRound(game);
    }

    private void startRound(QuizGame game) {
        Optional<RoundQuestion> round = game.currentQuestion();
        if (round.isEmpty()) {
            finish(game, "¡Se acabaron las preguntas!");
            return;
        }

        announceQuestion(game, round.get());
        runTimer(game, round.get().seconds(), secondsLeft -> {
            if (secondsLeft == 0) {
                resolveRound(game, round.get());
                return;
            }
            if (secondsLeft <= 5 || secondsLeft % 5 == 0) {
                broadcast(game, Component.text("Tiempo restante: " + secondsLeft + "s", NamedTextColor.GRAY));
            }
        });
    }

    private void resolveRound(QuizGame game, RoundQuestion round) {
        round.answers().stream()
                .filter(answer -> !answer.correct())
                .map(AnswerOption::floor)
                .forEach(floor -> arenaService.clear(floor.bounds(), game.arena().gameSpawn().getWorld()));

        broadcast(game, Component.text("¡Tiempo! Las respuestas incorrectas desaparecieron.", NamedTextColor.RED));
        game.advanceQuestion();

        if (!game.hasMoreQuestions()) {
            finish(game, "¡Partida terminada!");
            return;
        }

        runTimer(game, configuration.intermissionSeconds(), secondsLeft -> {
            if (secondsLeft == configuration.intermissionSeconds()) {
                broadcast(game, Component.text("Preparando la siguiente pregunta...", NamedTextColor.YELLOW));
            }
            if (secondsLeft == 0) {
                arenaService.restore(game.snapshot());
                startRound(game);
            }
        });
    }

    private void eliminate(QuizGame game, Player player) {
        game.player(player.getUniqueId()).ifPresent(state -> {
            if (!state.alive()) {
                return;
            }
            state.markEliminated();
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(game.arena().gameSpawn());
            broadcast(game, Component.text(player.getName() + " ha sido eliminado.", NamedTextColor.RED));
            if (!game.hasAlivePlayers()) {
                finish(game, "Todos los jugadores fueron eliminados.");
            }
        });
    }

    private void removePlayer(QuizGame game, UUID uniqueId, boolean restoreState) {
        game.removePlayer(uniqueId).ifPresent(state -> onlinePlayer(uniqueId).ifPresent(player -> {
            if (restoreState) {
                state.restore(player);
                player.teleport(configuration.lobbyLocation());
            }
        }));

        if (game.isEmpty()) {
            finish(game, "La partida quedó vacía.");
        }
    }

    private void finish(QuizGame game, String reason) {
        if (!games.contains(game)) {
            return;
        }
        game.cancelTask();
        game.state(QuizState.ENDING);
        broadcast(game, Component.text(reason, NamedTextColor.GOLD));

        runTimer(game, configuration.endingSeconds(), secondsLeft -> {
            if (secondsLeft == 0) {
                game.players().forEach(state -> onlinePlayer(state.uniqueId()).ifPresent(player -> {
                    state.restore(player);
                    player.teleport(configuration.lobbyLocation());
                }));
                arenaService.restore(game.snapshot());
                games.remove(game);
            }
        });
    }

    private void announceQuestion(QuizGame game, RoundQuestion round) {
        broadcast(game, Component.empty());
        broadcast(game, Component.text("Pregunta: ", NamedTextColor.AQUA).append(Component.text(round.prompt(), NamedTextColor.WHITE)));
        round.answers().forEach(answer -> broadcast(game,
                Component.text(answer.floor().label() + ": ", NamedTextColor.YELLOW)
                        .append(Component.text(answer.text(), NamedTextColor.WHITE))));
        broadcast(game, Component.text("Párate en la plataforma de la respuesta correcta.", NamedTextColor.GRAY));
    }

    private void broadcast(QuizGame game, Component message) {
        game.players().forEach(state -> onlinePlayer(state.uniqueId()).ifPresent(player -> player.sendMessage(message)));
    }

    private Optional<Player> onlinePlayer(UUID uniqueId) {
        return Optional.ofNullable(Bukkit.getPlayer(uniqueId));
    }

    private void runTimer(QuizGame game, int seconds, TimerTick tick) {
        game.currentTask(Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            private int secondsLeft = seconds;

            @Override
            public void run() {
                int currentSeconds = secondsLeft;
                if (currentSeconds <= 0) {
                    game.cancelTask();
                }
                tick.accept(currentSeconds);
                secondsLeft--;
            }
        }, 0L, 20L));
    }

    @FunctionalInterface
    private interface TimerTick {
        void accept(int secondsLeft);
    }
}
