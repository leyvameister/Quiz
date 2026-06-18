package me.ciakid.config;

import org.bukkit.World;

public record CuboidBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
    public com.sk89q.worldedit.regions.CuboidRegion toRegion(World world) {
        com.sk89q.worldedit.world.World adaptedWorld = com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(world);
        return new com.sk89q.worldedit.regions.CuboidRegion(
                adaptedWorld,
                com.sk89q.worldedit.math.BlockVector3.at(Math.min(minX, maxX), Math.min(minY, maxY), Math.min(minZ, maxZ)),
                com.sk89q.worldedit.math.BlockVector3.at(Math.max(minX, maxX), Math.max(minY, maxY), Math.max(minZ, maxZ))
        );
    }
}
