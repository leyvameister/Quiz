package me.ciakid.listener.quiz;

import me.ciakid.context.PlayerContext;
import me.ciakid.event.QuizPlayerJoinEvent;
import me.ciakid.game.Quiz;
import me.ciakid.player.QuizPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Objects;

public class QuizPlayerJoin implements Listener {

    @EventHandler
    public void onQuizPlayerJoin(QuizPlayerJoinEvent e) {
        Quiz quiz = e.getQuiz();
        QuizPlayer quizPlayer = e.getQuizPlayer();
        Location waitingSpawnpoint = quiz.getWaitingSpawnpoint();

        Player player = Bukkit.getPlayer(quizPlayer.getPlayer());
        quizPlayer.saveInventory();
        quizPlayer.setContext(PlayerContext.PLAYING);
        quizPlayer.setQuiz(quiz);
        quizPlayer.saveGameMode();
        Objects.requireNonNull(player).teleport(waitingSpawnpoint);
        player.getInventory().clear();

        if (readyToStart(quiz)) {
            quiz.start();
        }

    }

    private boolean readyToStart(Quiz quiz) {
        List<QuizPlayer> players = quiz.getPlayers();
        int minPlayers = quiz.getMinPlayers();

        return players.size() >= minPlayers;
    }
}
