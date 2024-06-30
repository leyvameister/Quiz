package me.ciakid.listener.quiz.round;

import me.ciakid.Plugin;
import me.ciakid.event.QuizRoundDelayRunningEvent;
import me.ciakid.game.Quiz;
import me.ciakid.game.model.Arena;
import me.ciakid.player.QuizPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class QuizRoundDelayRunning implements Listener {

    @EventHandler
    public void onRoundDelayRunning(QuizRoundDelayRunningEvent e) {
        Quiz quizEventsTest = e.getQuiz();
        int remainingDelayTime = e.getRemainingDelayTime();
        int roundDelayTime = quizEventsTest.getRoundDelayTime();
        List<QuizPlayer> players = quizEventsTest.getPlayers();
        Arena arena = quizEventsTest.getArena();

        if (remainingDelayTime == roundDelayTime / 2) {
            broadcast(players, Component.text("Prepare for the next round"));
            arena.repair();
        }

        if(remainingDelayTime == 0){
            broadcast(players, Component.text("Go!"));
        }

    }

    private void broadcast(List<QuizPlayer> players, TextComponent text) {
        players.forEach(quizPlayer -> {
            UUID playerUuid = quizPlayer.getPlayer();
            Player player = Bukkit.getPlayer(playerUuid);
            player.sendMessage(text);
        });
    }

}
