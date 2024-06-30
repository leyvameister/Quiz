package me.ciakid.listener.quiz;

import me.ciakid.Plugin;
import me.ciakid.context.PlayerContext;
import me.ciakid.event.QuizPlayerLeaveEvent;
import me.ciakid.game.Quiz;
import me.ciakid.player.QuizPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        QuizPlayer quizPlayer = QuizPlayer.wrap(player);

        if (!quizPlayer.getContext().equals(PlayerContext.NOT_PLAYING)) {
            Quiz quiz = quizPlayer.getQuiz();
            quiz.removePlayer(quizPlayer);
//            quiz.removePlayer(quizPlayer);
//            player.teleport(Plugin.getInstance().getLobby());
//            player.setGameMode(quizPlayer.getSavedGameMode());
//            player.getInventory().setContents(quizPlayer.getSavedInventory());
//            quizPlayer.destroy();
        }
    }
}
