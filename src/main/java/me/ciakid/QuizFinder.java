package me.ciakid;

import me.ciakid.exception.NoArenasAvailableException;
import me.ciakid.game.Quiz;
import me.ciakid.game.State;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.logging.Level;

public class QuizFinder implements GameFinder {
    @Override
    public Quiz findAvailable() throws NoArenasAvailableException {
        List<Quiz> liveQuizs = Plugin.getInstance().getQuizManager().getLiveQuizzes();

        if (liveQuizs.isEmpty()) {
            return Plugin.getInstance().getQuizManager().createNewQuiz();
        }

        for (Quiz quiz : liveQuizs) {
            if (!quiz.isFull() && quiz.getState().equals(State.WAITING)) {
                return quiz;
            }
        }
        return Plugin.getInstance().getQuizManager().createNewQuiz();
    }

}