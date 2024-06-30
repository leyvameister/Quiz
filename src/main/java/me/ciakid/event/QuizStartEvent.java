package me.ciakid.event;

import me.ciakid.game.Quiz;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuizStartEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Quiz quiz;

    public QuizStartEvent(Quiz quiz) {
        this.quiz = quiz;
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
}
