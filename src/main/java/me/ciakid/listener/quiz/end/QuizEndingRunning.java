package me.ciakid.listener.quiz.end;

import me.ciakid.Plugin;
import me.ciakid.event.QuizEndingRunningEvent;
import me.ciakid.game.Quiz;
import me.ciakid.game.State;
import me.ciakid.game.model.Arena;
import me.ciakid.listener.quiz.end.reason.QuizEndingReason;
import me.ciakid.player.QuizPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public class QuizEndingRunning implements Listener {

    @EventHandler
    public void onQuizEndingRunning(QuizEndingRunningEvent e) {
        Quiz quiz = e.getQuiz();
        Arena arena = quiz.getArena();
        QuizEndingReason quizEndingReason = e.getEndingReason();
        int remainingEndingTime = e.getRemainingEndingTime();
        List<QuizPlayer> players = quiz.getPlayers();

        if (quizEndingReason != null) {
            quizEndingReason.broadcastReason(quiz, remainingEndingTime);
        }

        if (remainingEndingTime == 0) {
            teleport(players, Plugin.getInstance().getLobby());
            players.forEach(quizPlayer -> {
                Player player = Bukkit.getPlayer(quizPlayer.getPlayer());
                if (player != null) {
                    player.setGameMode(quizPlayer.getSavedGameMode());
                    player.getInventory().setContents(quizPlayer.getSavedInventory());
                }
                quizPlayer.destroy();
            });
            players.clear();
            arena.repair();
            Plugin.getInstance().getQuizManager().unregister(quiz);
        }
    }

    private void teleport(List<QuizPlayer> players, Location location) {
        players.forEach(quizPlayer -> {
            UUID playerUuid = quizPlayer.getPlayer();
            Player player = Bukkit.getPlayer(playerUuid);
            player.teleport(location);
        });
    }

}
