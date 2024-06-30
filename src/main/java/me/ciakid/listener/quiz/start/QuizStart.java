package me.ciakid.listener.quiz.start;

import me.ciakid.Plugin;
import me.ciakid.event.QuizCountdownRunningEvent;
import me.ciakid.event.QuizStartEvent;
import me.ciakid.game.Quiz;
import me.ciakid.game.State;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class QuizStart implements Listener {


    @EventHandler
    public void onQuizStart(QuizStartEvent e) {
        Quiz quizEventsTest = e.getQuiz();

        quizEventsTest.setState(State.STARTING);

        BukkitRunnable countdownTask = new BukkitRunnable() {
            int remainingCountdownTime = quizEventsTest.getCountdownTime();

            @Override
            public void run() {
                if(quizEventsTest.isCanceled()){
                    cancel();
                    return;
                }

                if (remainingCountdownTime >= 0) {
                    Bukkit.getPluginManager().callEvent(new QuizCountdownRunningEvent(quizEventsTest, remainingCountdownTime));
                    remainingCountdownTime--;
                }
            }
        };
        countdownTask.runTaskTimer(Plugin.getInstance(), 0L, 20L);
    }
}
