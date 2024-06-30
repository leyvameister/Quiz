package me.ciakid.listener.quiz.end;

import me.ciakid.game.Quiz;

public interface QuizEndingReason {
    void broadcastReason(Quiz quiz, int remainingEndingTime);
}
