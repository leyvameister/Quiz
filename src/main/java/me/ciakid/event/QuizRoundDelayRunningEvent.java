package me.ciakid.event;

import me.ciakid.game.Quiz;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuizRoundDelayRunningEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Quiz quiz;
    private final int remainingDelayTime;

    public QuizRoundDelayRunningEvent(Quiz quiz, int remainingDelayTime) {
        this.quiz = quiz;
        this.remainingDelayTime = remainingDelayTime;
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

    public int getRemainingDelayTime() {
        return remainingDelayTime;
    }
}
