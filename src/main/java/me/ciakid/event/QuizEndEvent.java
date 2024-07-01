package me.ciakid.event;

import me.ciakid.game.Quiz;
import me.ciakid.listener.quiz.end.QuizEndingReason;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuizEndEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Quiz quiz;
    private QuizEndingReason quizEndingReason;

    public QuizEndEvent(Quiz quiz, QuizEndingReason quizEndingReason) {
        this.quiz = quiz;
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

    public QuizEndingReason getEndingReason() {
        return this.quizEndingReason;
    }
}
