package me.ciakid.game;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class LivesSystem {

    private final HashMap<UUID, Byte> playerLivesMap;

    public LivesSystem(HashMap<UUID, Byte> playerLivesMap) {
        this.playerLivesMap = playerLivesMap;
    }

    public byte livesOf(Player player) {
        return playerLivesMap.get(player.getUniqueId());
    }

    public void decreaseLivesFor(Player player) {
        Byte currentLives = playerLivesMap.get(player.getUniqueId());
        playerLivesMap.put(player.getUniqueId(), (byte) (currentLives-1));
    }
}
