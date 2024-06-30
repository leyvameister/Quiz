package me.ciakid.util;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public final class Utils {

    public static void teleport(List<UUID> players, Location location) {
        for (UUID player : players) {
            Bukkit.getPlayer(player).teleport(location);
        }
    }

    public static void broadcast(List<UUID> players, TextComponent message) {
        for (UUID player : players) {
            Bukkit.getPlayer(player).sendMessage(message);
        }
    }
}
