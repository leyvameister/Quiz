package me.ciakid.listener.quiz.end;

import me.ciakid.game.Quiz;
import me.ciakid.player.QuizPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class QuizAllPlayersLostEndReason implements QuizEndingReason {

    @Override
    public void broadcastReason(Quiz quiz, int remainingEndingTime) {
        int endingTime = quiz.getEndingTime();
        List<QuizPlayer> players = quiz.getPlayers();

        if (remainingEndingTime == endingTime / 2) {
            broadcast(players, Component.text("All players have lost, cya."));
        }

    }


    private void broadcast(List<QuizPlayer> players, TextComponent text) {
        players.forEach(quizPlayer -> {
            UUID playerUuid = quizPlayer.getPlayer();
            Player player = Bukkit.getPlayer(playerUuid);
            Objects.requireNonNull(player).sendMessage(text);
        });
    }
}
