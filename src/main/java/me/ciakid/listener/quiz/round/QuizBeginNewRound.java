package me.ciakid.listener.quiz.round;

import me.ciakid.Plugin;
import me.ciakid.event.*;
import me.ciakid.game.Quiz;
import me.ciakid.game.model.IQuestion;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class QuizBeginNewRound implements Listener {

    @EventHandler
    public void onQuizBeginNewRound(QuizBeginNewRoundEvent e) {
        Quiz quizEventsTest = e.getQuiz();
        List<IQuestion> questions = quizEventsTest.getQuestions();
        int currentQuestionIndex = quizEventsTest.getCurrentQuestionIndex();
        int roundDelayTime = quizEventsTest.getRoundDelayTime();

        IQuestion currentQuestion = questions.get(currentQuestionIndex);

        BukkitRunnable roundTask = new BukkitRunnable() {
            int remainingDelayTime = roundDelayTime;
            int remainingQuestionTime = currentQuestion.getTimeToAnswer();
            boolean delayMoment = true;
            boolean roundRunningMoment = false;

            @Override
            public void run() {

                if(quizEventsTest.isCanceled()){
                    cancel();
                    return;
                }

                // Handle delay moment
                if (delayMoment) {
                    if (remainingDelayTime >= 0) {
                        Bukkit.getPluginManager().callEvent(new QuizRoundDelayRunningEvent(quizEventsTest, remainingDelayTime));
                    } else {
                        delayMoment = false;
                        roundRunningMoment = true;
                    }

                    remainingDelayTime--;
                }

                // Handle round running moment
                if (roundRunningMoment) {
                    if (remainingQuestionTime >= 0) {
                        Bukkit.getPluginManager().callEvent(new QuizRoundRunningEvent(quizEventsTest, remainingQuestionTime));
                    } else {
                        roundRunningMoment = false;
                        cancel();
                    }

                    remainingQuestionTime--;
                }
            }
        };
        roundTask.runTaskTimer(Plugin.getInstance(), 0L, 20L);

    }
}
