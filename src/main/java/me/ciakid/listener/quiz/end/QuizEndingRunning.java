package me.ciakid.listener.quiz.end;

import me.ciakid.Plugin;
import me.ciakid.event.QuizEndingRunningEvent;
import me.ciakid.game.Quiz;
import me.ciakid.game.State;
import me.ciakid.game.model.Arena;
import me.ciakid.player.QuizPlayer;
import net.kyori.adventure.text.TextComponent;
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
        Quiz quizEventsTest = e.getQuiz();
        Arena arena = quizEventsTest.getArena();
        QuizEndingReason quizEndingReason = e.getEndingReason();
        int remainingEndingTime = e.getRemainingEndingTime();
        List<QuizPlayer> players = quizEventsTest.getPlayers();

        if (quizEndingReason != null) {
            quizEndingReason.broadcastReason(quizEventsTest, remainingEndingTime);
        }

        quizEventsTest.setState(State.ENDING);

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
            Plugin.getInstance().getQuizManager().unregister(quizEventsTest);
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
