package me.ciakid.game.model;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import net.kyori.adventure.text.format.TextColor;

public class ColoredQuizFloor extends QuizFloor{

    private final TextColor color;

    public ColoredQuizFloor(World world, BlockVector3 pos1, BlockVector3 pos2, TextColor color) {
        super(world, pos1, pos2);
        this.color = color;
    }

    public TextColor getColor() {
        return color;
    }
}
