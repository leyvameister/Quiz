package me.ciakid.arena;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;

public record ArenaSnapshot(CuboidRegion region, Clipboard clipboard, BlockVector3 pasteOrigin) {
}
