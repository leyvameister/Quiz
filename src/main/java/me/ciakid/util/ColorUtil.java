package me.ciakid.util;

import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.Map;

public class ColorUtil {
    private static final Map<String, NamedTextColor> colorMap = new HashMap<>();

    static {
        colorMap.put("red", NamedTextColor.RED);
        colorMap.put("blue", NamedTextColor.BLUE);
        colorMap.put("yellow", NamedTextColor.YELLOW);
        colorMap.put("green", NamedTextColor.GREEN);
    }

    public static NamedTextColor getNamedTextColor(String colorName) {
        return colorMap.getOrDefault(colorName.toLowerCase(), NamedTextColor.WHITE);
    }
}