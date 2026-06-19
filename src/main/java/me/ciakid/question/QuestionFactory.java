package me.ciakid.question;

import me.ciakid.model.AnswerOption;
import me.ciakid.model.FloorDefinition;
import me.ciakid.model.QuestionDefinition;
import me.ciakid.model.RoundQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class QuestionFactory {
    private final Random random = new Random();

    public List<RoundQuestion> createRounds(List<QuestionDefinition> questionPool, List<FloorDefinition> floors, int roundCount) {
        if (floors.size() < 2) {
            throw new IllegalArgumentException("At least two floors are required.");
        }

        List<QuestionDefinition> shuffledQuestions = new ArrayList<>(questionPool);
        Collections.shuffle(shuffledQuestions, random);
        List<RoundQuestion> rounds = new ArrayList<>();

        for (int index = 0; index < roundCount; index++) {
            QuestionDefinition question = shuffledQuestions.get(index % shuffledQuestions.size());
            rounds.add(createRound(question, floors));
        }
        return rounds;
    }

    private RoundQuestion createRound(QuestionDefinition question, List<FloorDefinition> floors) {
        List<String> wrongAnswers = new ArrayList<>(question.wrongAnswers());
        Collections.shuffle(wrongAnswers, random);

        int answersToUse = Math.min(floors.size(), wrongAnswers.size() + 1);
        List<FloorDefinition> shuffledFloors = new ArrayList<>(floors);
        Collections.shuffle(shuffledFloors, random);

        List<AnswerOption> answers = new ArrayList<>();
        answers.add(new AnswerOption(question.correctAnswer(), shuffledFloors.get(0), true));
        for (int index = 1; index < answersToUse; index++) {
            answers.add(new AnswerOption(wrongAnswers.get(index - 1), shuffledFloors.get(index), false));
        }
        Collections.shuffle(answers, random);
        return new RoundQuestion(question.prompt(), answers, question.seconds());
    }
}
