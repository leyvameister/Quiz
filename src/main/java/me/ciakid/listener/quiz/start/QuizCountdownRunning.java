package me.ciakid.listener.quiz.start;

import me.ciakid.Plugin;
import me.ciakid.event.QuizCountdownRunningEvent;
import me.ciakid.game.Quiz;
import me.ciakid.game.State;
import me.ciakid.player.QuizPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;


public class QuizCountdownRunning implements Listener {

    @EventHandler
    public void onCountdownRunning(QuizCountdownRunningEvent e) {
        Quiz quiz = e.getQuiz();
        int remainingCountdownTime = e.getRemainingCountdownTime();

        broadcast(quiz.getPlayers(), Component.text("Game starting in: " + remainingCountdownTime));

        if (remainingCountdownTime == 0) {
            List<QuizPlayer> players = quiz.getPlayers();
            Location arenaSpawnpoint = quiz.getArenaSpawnpoint();

            broadcast(players, Component.text("Go!"));
            teleport(players, arenaSpawnpoint);
        }
    }

    private void broadcast(List<QuizPlayer> players, TextComponent text) {
        players.forEach(quizPlayer -> {
            UUID playerUuid = quizPlayer.getPlayer();
            Player player = Bukkit.getPlayer(playerUuid);
            player.sendMessage(text);
        });
    }

    private void teleport(List<QuizPlayer> players, Location location) {
        players.forEach(quizPlayer -> {
            UUID playerUuid = quizPlayer.getPlayer();
            Player player = Bukkit.getPlayer(playerUuid);
            player.teleport(location);
        });
    }

}
