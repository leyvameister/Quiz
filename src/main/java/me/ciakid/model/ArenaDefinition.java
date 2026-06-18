package me.ciakid.model;

import me.ciakid.config.CuboidBounds;
import org.bukkit.Location;

import java.util.List;

public record ArenaDefinition(
        String id,
        int minPlayers,
        int maxPlayers,
        int rounds,
        Location waitingSpawn,
        Location gameSpawn,
        CuboidBounds bounds,
        List<FloorDefinition> floors
) {
    public ArenaDefinition {
        floors = List.copyOf(floors);
    }
}
