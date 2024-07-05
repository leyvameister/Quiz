package me.ciakid.game.model;

import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public interface IQuestion {
    int getTimeToAnswer();

    TextComponent getQuestionText();

    Answer getRightAnswer();

    List<Answer> getAllAnswers();

    List<Answer> getWrongAnswers();
}
