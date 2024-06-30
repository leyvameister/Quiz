package me.ciakid.game.model;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.block.Block;


public class QuizFloor extends CuboidRegion implements Floor {

    public QuizFloor(World world, BlockVector3 pos1, BlockVector3 pos2) {
        super(world, pos1, pos2);
    }

    @Override
    public void destroy() {
        for (BlockVector3 blockVector : this) {
            org.bukkit.World bukkitWorld = BukkitAdapter.adapt(world);
            Block block = bukkitWorld.getBlockAt(blockVector.getBlockX(), blockVector.getBlockY(),
                    blockVector.getBlockZ());
            block.setType(org.bukkit.Material.AIR);
        }
    }

}
