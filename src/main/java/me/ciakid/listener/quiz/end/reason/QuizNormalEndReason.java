package me.ciakid.listener.quiz.end.reason;

import me.ciakid.game.Quiz;
import me.ciakid.game.model.Arena;
import me.ciakid.listener.quiz.end.reason.QuizEndingReason;
import me.ciakid.player.QuizPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class QuizNormalEndReason implements QuizEndingReason {
    @Override
    public void broadcastReason(Quiz quiz, int remainingEndingTime) {
        int endingTime = quiz.getEndingTime();
        Arena arena = quiz.getArena();
        List<QuizPlayer> players = quiz.getPlayers();

        if (remainingEndingTime == Math.floor(endingTime / 1.5)) {
            broadcast(players, Component.text("Quiz completed, congratulations to the winners."));
            arena.repair();
        }

        if (remainingEndingTime == Math.floor(endingTime / 3.5)) {
            broadcast(players, Component.text("Hope to see you here next time, Bye!"));
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
