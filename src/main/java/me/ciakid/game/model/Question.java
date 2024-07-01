package me.ciakid.game.model;

import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class Question implements IQuestion {
    private final TextComponent questionText;
    private final Answer rightAnswer;
    private Answer wrongAnswer1;
    private Answer wrongAnswer2;
    private Answer wrongAnswer3;
    private final int timeToAnswer;


    public Question(TextComponent questionText, Answer rightAnswer, Answer wrongAnswer1, Answer wrongAnswer2, Answer wrongAnswer3, int timeToAnswer) {
        this.questionText = questionText;
        this.rightAnswer = rightAnswer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
        this.wrongAnswer3 = wrongAnswer3;
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
    public Answer getWrongAnswer1() {
        return this.wrongAnswer1;
    }

    @Override
    public Answer getWrongAnswer2() {
        return this.wrongAnswer2;
    }

    @Override
    public Answer getWrongAnswer3() {
        return this.wrongAnswer3;
    }

    @Override
    public List<Answer> getAllAnswers() {
        return List.of(wrongAnswer1, wrongAnswer2, wrongAnswer3, rightAnswer);
    }

    @Override
    public List<Answer> getWrongAnswers() {
        return List.of(wrongAnswer1, wrongAnswer2, wrongAnswer3);
    }

}
