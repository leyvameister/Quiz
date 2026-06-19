package me.ciakid.model;

import java.util.List;

public record QuestionDefinition(String prompt, String correctAnswer, List<String> wrongAnswers, int seconds) {
    public QuestionDefinition {
        wrongAnswers = List.copyOf(wrongAnswers);
    }
}
