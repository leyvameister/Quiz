package me.ciakid.listener.quiz;

import me.ciakid.context.PlayerContext;
import me.ciakid.event.QuizPlayerLoseEvent;
import me.ciakid.game.Quiz;
import me.ciakid.listener.quiz.end.reason.QuizAllPlayersLostEndReason;
import me.ciakid.player.QuizPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Objects;

public class QuizPlayerLose implements Listener {

    @EventHandler
    public void onQuizPlayerLose(QuizPlayerLoseEvent e) {
        Quiz quiz = e.getQuiz();
        QuizPlayer quizPlayer = e.getQuizPlayer();
        Player player = Bukkit.getPlayer(quizPlayer.getPlayer());

        setPlayerToSpectator(Objects.requireNonNull(player), quiz);
        updatePlayerContext(quizPlayer);

        if (!alivePlayerLeft(quiz)) {
            quiz.end(new QuizAllPlayersLostEndReason());
        }


    }

    private boolean alivePlayerLeft(Quiz quiz) {
        List<QuizPlayer> players = quiz.getPlayers();
        for (QuizPlayer quizPlayer : players) {
            if (quizPlayer.getContext().equals(PlayerContext.PLAYING)) {
                return true;
            }
        }
        return false;
    }

    private void setPlayerToSpectator(Player player, Quiz quiz) {
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(quiz.getArenaSpawnpoint());
    }

    private void updatePlayerContext(QuizPlayer quizPlayer) {
        quizPlayer.setContext(PlayerContext.SPECTATING);
    }
}
