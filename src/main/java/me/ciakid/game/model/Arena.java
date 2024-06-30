package me.ciakid.game.model;

import com.sk89q.worldedit.regions.CuboidRegion;

public interface Arena {
    void repair();

    CuboidRegion getCuboidRegion();
}
