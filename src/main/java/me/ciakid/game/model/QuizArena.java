package me.ciakid.game.model;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class QuizArena implements Arena {
    private final CuboidRegion cuboidRegion;
    private final Clipboard clipboard;

    public QuizArena(CuboidRegion cuboidRegion) {
        this.cuboidRegion = cuboidRegion;
        this.clipboard = new BlockArrayClipboard(cuboidRegion);

        try {
            Operations.complete(new ForwardExtentCopy(
                    cuboidRegion.getWorld(), cuboidRegion, this.clipboard, cuboidRegion.getMinimumPoint()));
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void repair() {
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(this.cuboidRegion.getWorld())) {
            Operation operation = new ClipboardHolder(this.clipboard).createPaste(editSession)//.to(location)
                    // configure here
                    .to(this.cuboidRegion.getMinimumPoint())
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CuboidRegion getCuboidRegion() {
        return this.cuboidRegion;
    }

}
