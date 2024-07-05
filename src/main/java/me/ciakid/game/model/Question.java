package me.ciakid.game.model;

import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class Question implements IQuestion {
    private final TextComponent questionText;
    private final Answer rightAnswer;
    private final List<Answer> wrongAnswers;
    private final int timeToAnswer;


    public Question(TextComponent questionText, Answer rightAnswer, List<Answer> wrongAnswers, int timeToAnswer) {
        this.questionText = questionText;
        this.rightAnswer = rightAnswer;
        this.wrongAnswers = wrongAnswers;
        this.timeToAnswer = timeToAnswer;
    }

    @Override
    public int getTimeToAnswer() {
        return this.timeToAnswer;
    }

    @Override
    public TextComponent getQuestionText() {
        return this.questionText;
    }

    @Override
    public Answer getRightAnswer() {
        return rightAnswer;
    }

    @Override
    public List<Answer> getAllAnswers() {
        List<Answer> allAnswers = new ArrayList<>(wrongAnswers);
        allAnswers.add(rightAnswer);
        return allAnswers;
    }

    @Override
    public List<Answer> getWrongAnswers() {
        return wrongAnswers;
    }

}
