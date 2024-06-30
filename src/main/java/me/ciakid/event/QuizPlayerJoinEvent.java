package me.ciakid.event;

import me.ciakid.game.Quiz;
import me.ciakid.player.QuizPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuizPlayerJoinEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Quiz quiz;
    private final QuizPlayer quizPlayer;

    public QuizPlayerJoinEvent(Quiz quiz, QuizPlayer quizPlayer) {
        this.quiz = quiz;
        this.quizPlayer = quizPlayer;
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

    public QuizPlayer getQuizPlayer() {
        return quizPlayer;
    }
}
