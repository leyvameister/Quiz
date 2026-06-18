package me.ciakid.model;

import java.util.List;

public record RoundQuestion(String prompt, List<AnswerOption> answers, int seconds) {
    public RoundQuestion {
        answers = List.copyOf(answers);
    }
}
