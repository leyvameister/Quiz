package me.ciakid.listener.quiz;

import me.ciakid.Plugin;
import me.ciakid.context.PlayerContext;
import me.ciakid.event.QuizPlayerLeaveEvent;
import me.ciakid.game.Quiz;
import me.ciakid.game.State;
import me.ciakid.player.QuizPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class QuizPlayerLeave implements Listener {

    @EventHandler
    public void onQuizPlayerLeave(QuizPlayerLeaveEvent e) {
        Quiz quiz = e.getQuiz();
        QuizPlayer quizPlayer = e.getQuizPlayer();
        Player player = Bukkit.getPlayer(quizPlayer.getPlayer());

        player.teleport(Plugin.getInstance().getLobby());
        player.setGameMode(quizPlayer.getSavedGameMode());
        player.getInventory().setContents(quizPlayer.getSavedInventory());
        quizPlayer.setContext(PlayerContext.NOT_PLAYING);
        quizPlayer.setQuiz(null);
        quizPlayer.destroy();

        if (quiz.getPlayers().isEmpty() && quiz.getState() != State.WAITING) {
            quiz.end(null);
        }
    }
}
