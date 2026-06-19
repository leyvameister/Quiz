package me.ciakid.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class QuizPlayerState {
    private final UUID uniqueId;
    private final ItemStack[] inventoryContents;
    private final ItemStack[] armorContents;
    private final GameMode gameMode;
    private boolean alive = true;

    private QuizPlayerState(UUID uniqueId, ItemStack[] inventoryContents, ItemStack[] armorContents, GameMode gameMode) {
        this.uniqueId = uniqueId;
        this.inventoryContents = inventoryContents;
        this.armorContents = armorContents;
        this.gameMode = gameMode;
    }

    public static QuizPlayerState capture(Player player) {
        return new QuizPlayerState(
                player.getUniqueId(),
                player.getInventory().getContents(),
                player.getInventory().getArmorContents(),
                player.getGameMode()
        );
    }

    public UUID uniqueId() {
        return uniqueId;
    }

    public boolean alive() {
        return alive;
    }

    public void markEliminated() {
        this.alive = false;
    }

    public void restore(Player player) {
        player.getInventory().setContents(inventoryContents);
        player.getInventory().setArmorContents(armorContents);
        player.setGameMode(gameMode);
    }
}
