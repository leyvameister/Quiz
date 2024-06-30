package me.ciakid.event;

import me.ciakid.game.Quiz;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuizRoundRunningEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Quiz quiz;
    private final int remainingRoundTime;

    public QuizRoundRunningEvent(Quiz quiz, int remainingRoundTime) {
        this.quiz = quiz;
        this.remainingRoundTime = remainingRoundTime;
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
        return remainingRoundTime;
    }
}
