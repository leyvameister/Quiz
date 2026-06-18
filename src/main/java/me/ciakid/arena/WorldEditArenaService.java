package me.ciakid.arena;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.ciakid.config.CuboidBounds;
import org.bukkit.World;

public final class WorldEditArenaService {
    public ArenaSnapshot capture(CuboidBounds bounds, World world) {
        CuboidRegion region = bounds.toRegion(world);
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        try {
            Operations.complete(new ForwardExtentCopy(region.getWorld(), region, clipboard, region.getMinimumPoint()));
            return new ArenaSnapshot(region, clipboard, region.getMinimumPoint());
        } catch (WorldEditException exception) {
            throw new IllegalStateException("Could not capture arena snapshot.", exception);
        }
    }

    public void restore(ArenaSnapshot snapshot) {
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(snapshot.region().getWorld())) {
            Operation operation = new ClipboardHolder(snapshot.clipboard())
                    .createPaste(editSession)
                    .to(snapshot.pasteOrigin())
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException exception) {
            throw new IllegalStateException("Could not restore arena snapshot.", exception);
        }
    }

    public void clear(CuboidBounds bounds, World world) {
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(bounds.toRegion(world).getWorld())) {
            CuboidRegion region = bounds.toRegion(world);
            editSession.setBlocks(region, BlockTypes.AIR.getDefaultState());
        } catch (WorldEditException exception) {
            throw new IllegalStateException("Could not clear answer floor.", exception);
        }
    }

    public boolean isBelow(CuboidBounds bounds, org.bukkit.Location location) {
        return location.getBlockY() < Math.min(bounds.minY(), bounds.maxY());
    }
}
