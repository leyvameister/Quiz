package me.ciakid.game;

import me.ciakid.arena.ArenaSnapshot;
import me.ciakid.model.ArenaDefinition;
import me.ciakid.model.RoundQuestion;
import me.ciakid.player.QuizPlayerState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class QuizGame {
    private final ArenaDefinition arena;
    private final ArenaSnapshot snapshot;
    private final List<RoundQuestion> questions;
    private final Map<UUID, QuizPlayerState> players = new LinkedHashMap<>();
    private QuizState state = QuizState.WAITING;
    private int questionIndex;
    private BukkitTask currentTask;

    public QuizGame(ArenaDefinition arena, ArenaSnapshot snapshot, List<RoundQuestion> questions) {
        this.arena = arena;
        this.snapshot = snapshot;
        this.questions = List.copyOf(questions);
    }

    public ArenaDefinition arena() {
        return arena;
    }

    public ArenaSnapshot snapshot() {
        return snapshot;
    }

    public QuizState state() {
        return state;
    }

    public void state(QuizState state) {
        this.state = state;
    }

    public int questionIndex() {
        return questionIndex;
    }

    public void advanceQuestion() {
        questionIndex++;
    }

    public Optional<RoundQuestion> currentQuestion() {
        if (questionIndex >= questions.size()) {
            return Optional.empty();
        }
        return Optional.of(questions.get(questionIndex));
    }

    public boolean hasMoreQuestions() {
        return questionIndex < questions.size();
    }

    public Collection<QuizPlayerState> players() {
        return players.values();
    }

    public int playerCount() {
        return players.size();
    }

    public boolean isFull() {
        return players.size() >= arena.maxPlayers();
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean hasPlayer(UUID uniqueId) {
        return players.containsKey(uniqueId);
    }

    public void addPlayer(Player player) {
        players.put(player.getUniqueId(), QuizPlayerState.capture(player));
    }

    public Optional<QuizPlayerState> removePlayer(UUID uniqueId) {
        return Optional.ofNullable(players.remove(uniqueId));
    }

    public Optional<QuizPlayerState> player(UUID uniqueId) {
        return Optional.ofNullable(players.get(uniqueId));
    }

    public boolean hasAlivePlayers() {
        return players.values().stream().anyMatch(QuizPlayerState::alive);
    }

    public BukkitTask currentTask() {
        return currentTask;
    }

    public void currentTask(BukkitTask currentTask) {
        cancelTask();
        this.currentTask = currentTask;
    }

    public void cancelTask() {
        if (currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }
    }
}
