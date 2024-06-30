package me.ciakid.player;

import me.ciakid.context.PlayerContext;
import me.ciakid.game.Game;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface GamePlayer {
    UUID getPlayer();

    void setContext(PlayerContext context);

    PlayerContext getContext();

    ItemStack[] getSavedInventory();

    void saveInventory();

    GameMode getSavedGameMode();

    void saveGameMode();
}
