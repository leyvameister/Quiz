package me.ciakid.game;

import me.ciakid.game.model.Arena;
import me.ciakid.player.QuizPlayer;
import org.bukkit.Location;

public interface Game {
    void start();

    void addPlayer(QuizPlayer quizPlayer);

    void removePlayer(QuizPlayer quizPlayer);

    Location getWaitingSpawnpoint();

    Location getArenaSpawnpoint();

    Arena getArena();

}
