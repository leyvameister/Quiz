package me.ciakid.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

public class ColoredString {

    private final TextComponent textComponent;
    private TextColor textColor;

    public ColoredString(String content, TextColor textColor) {
        this.textComponent = Component.text(content).color(textColor);
    }

    public TextComponent getTextComponent() {
        return textComponent;
    }

    public TextColor getTextColor() {
        return textColor;
    }

    public void setTextColor(TextColor textColor) {
        this.textColor = textColor;
    }
}

