package me.ciakid.game.model;

import me.ciakid.component.ColoredString;

public class Answer {

    private ColoredString answerText;

    private final Floor floor;

    public Answer(ColoredString answerText, Floor quizFloor) {
        this.answerText = answerText;
        this.floor = quizFloor;
    }

    public ColoredString getAnswerText() {
        return answerText;
    }

    public void setAnswerText(ColoredString answerText) {
        this.answerText = answerText;
    }

    public Floor getFloor() {
        return floor;
    }
}
