package me.ciakid.game.model;

import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public interface IQuestion {
    int getTimeToAnswer();

    TextComponent getQuestionText();

    Answer getRightAnswer();

    Answer getWrongAnswer1();

    Answer getWrongAnswer2();

    Answer getWrongAnswer3();

    List<Answer> getAllAnswers();

    List<Answer> getWrongAnswers();
}
