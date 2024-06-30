package me.ciakid.listener.quiz;

import com.sk89q.worldedit.regions.CuboidRegion;
import me.ciakid.Plugin;
import me.ciakid.context.PlayerContext;
import me.ciakid.event.QuizPlayerLoseEvent;
import me.ciakid.game.Quiz;
import me.ciakid.game.model.Arena;
import me.ciakid.player.QuizPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        QuizPlayer quizPlayer = QuizPlayer.wrap(player);

        if (isPlayerPlaying(quizPlayer)) {
            handlePlayerInGame(player, quizPlayer);
        }
    }

    private boolean isPlayerPlaying(QuizPlayer quizPlayer) {
        return quizPlayer.getContext() == PlayerContext.PLAYING;
    }

    private void handlePlayerInGame(Player player, QuizPlayer quizPlayer) {
        Quiz quiz = quizPlayer.getQuiz();
        Arena arena = quiz.getArena();
        CuboidRegion cuboidRegion = arena.getCuboidRegion();
        int minimumY = cuboidRegion.getMinimumY();

        if (isPlayerBelowMinimumY(player, minimumY)) {
            Bukkit.getPluginManager().callEvent(new QuizPlayerLoseEvent(quiz, quizPlayer));
        }
    }

    private boolean isPlayerBelowMinimumY(Player player, int minimumY) {
        return player.getLocation().getBlockY() < minimumY;
    }

}
