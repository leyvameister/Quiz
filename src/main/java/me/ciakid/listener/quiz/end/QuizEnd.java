package me.ciakid.listener.quiz.end;

import me.ciakid.Plugin;
import me.ciakid.event.QuizEndEvent;
import me.ciakid.event.QuizEndingRunningEvent;
import me.ciakid.game.Quiz;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class QuizEnd implements Listener {

    @EventHandler
    public void onQuizEnd(QuizEndEvent e) {
        Quiz quizEventsTest = e.getQuiz();
        int endingTime = quizEventsTest.getEndingTime();
        QuizEndingReason quizEndingReason = e.getEndingReason();

        BukkitRunnable endingTask = new BukkitRunnable() {
            int remainingEndingTime = endingTime;

            @Override
            public void run() {

                if (remainingEndingTime >= 0) {
                    Bukkit.getPluginManager().callEvent(new QuizEndingRunningEvent(quizEventsTest, remainingEndingTime, quizEndingReason));
                    remainingEndingTime--;
                }
            }
        };
        endingTask.runTaskTimer(Plugin.getInstance(), 0L, 20L);
    }
}
