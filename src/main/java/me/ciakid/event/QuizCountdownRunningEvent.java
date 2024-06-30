package me.ciakid.event;

import me.ciakid.game.Quiz;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuizCountdownRunningEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Quiz quiz;
    private final int remainingCountdownTime;

    public QuizCountdownRunningEvent(Quiz quiz, int remainingCountdownTime) {
        this.quiz = quiz;
        this.remainingCountdownTime = remainingCountdownTime;
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

    public int getRemainingCountdownTime() {
        return remainingCountdownTime;
    }
}
