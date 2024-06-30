package me.ciakid.event;

import me.ciakid.game.Quiz;
import me.ciakid.listener.quiz.end.QuizEndingReason;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuizEndingRunningEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Quiz quiz;
    private final int remainingEndingTime;
    private QuizEndingReason quizEndingReason;

    public QuizEndingRunningEvent(Quiz quiz, int remainingEndingTime, QuizEndingReason quizEndingReason) {
        this.quiz = quiz;
        this.remainingEndingTime = remainingEndingTime;
        this.quizEndingReason = quizEndingReason;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public int getRemainingEndingTime() {
        return remainingEndingTime;
    }

    public QuizEndingReason getEndingReason() {
        return this.quizEndingReason;
    }
}
