package me.ciakid.game;


import me.ciakid.Plugin;
import me.ciakid.event.*;
import me.ciakid.game.model.Arena;
import me.ciakid.game.model.IQuestion;
import me.ciakid.listener.quiz.end.reason.QuizEndingReason;
import me.ciakid.listener.quiz.end.reason.QuizNormalEndReason;
import me.ciakid.player.QuizPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Quiz implements Game {
    protected String name;
    protected State state;
    protected List<QuizPlayer> players;
    protected int maxPlayers;
    protected int minPlayers;
    protected Location waitingSpawnpoint;
    protected Location arenaSpawnpoint;
    protected List<IQuestion> questions;
    protected Arena arena;
    protected final int countdownTime;
    protected final int roundDelayTime;
    protected final int endingTime;
    protected int currentQuestionIndex;

    public Quiz(String name, int maxPlayers, int minPlayers, Location waitingSpawnpoint, Location arenaSpawnpoint, List<IQuestion> questions, Arena arena, int countdownTime, int roundDelayTime, int endingTime) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.waitingSpawnpoint = waitingSpawnpoint;
        this.arenaSpawnpoint = arenaSpawnpoint;
        this.questions = questions;
        this.arena = arena;
        this.countdownTime = countdownTime;
        this.roundDelayTime = roundDelayTime;
        this.endingTime = endingTime;

        this.state = State.WAITING;
        this.players = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

    public List<QuizPlayer> getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }


    public int getMinPlayers() {
        return minPlayers;
    }

    @Override
    public Location getWaitingSpawnpoint() {
        return waitingSpawnpoint;
    }

    @Override
    public Location getArenaSpawnpoint() {
        return arenaSpawnpoint;
    }

    public List<IQuestion> getQuestions() {
        return questions;
    }

    @Override
    public Arena getArena() {
        return arena;
    }

    public int getCountdownTime() {
        return countdownTime;
    }

    public int getRoundDelayTime() {
        return roundDelayTime;
    }

    public int getEndingTime() {
        return endingTime;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public void addPlayer(QuizPlayer quizPlayer) {
        this.players.add(quizPlayer);
        Bukkit.getPluginManager().callEvent(new QuizPlayerJoinEvent(this, quizPlayer));
    }

    public void removePlayer(QuizPlayer quizPlayer) {
        players.remove(quizPlayer);
        Bukkit.getPluginManager().callEvent(new QuizPlayerLeaveEvent(this, quizPlayer));
    }

    public void start() {
        this.state = State.STARTING;
        startCountdown();
    }

    protected void startCountdown() {
        new BukkitRunnable() {
            int remainingCountdownTime = countdownTime;

            @Override
            public void run() {
                if (state.equals(State.ENDING)) {
                    cancel();
                    return;
                }

                if (remainingCountdownTime >= 0) {
                    onEachSecondCountdown(remainingCountdownTime);
                    if (remainingCountdownTime == 0) {
                        cancel();
                    }
                    remainingCountdownTime--;
                }
            }
        }.runTaskTimer(Plugin.getInstance(), 0L, 20L);
    }

    protected void onEachSecondCountdown(int secondsLeft) {
        Bukkit.getPluginManager().callEvent(new QuizCountdownRunningEvent(Quiz.this, secondsLeft));
        if (secondsLeft == 0) {
            runNewRound();
        }
    }

    protected void runNewRound() {
        state = State.STARTED;
        if (isFirstRound()) {
            startQuestionTimer();
        } else {
            startDelayTimer();
        }
    }

    private void startQuestionTimer() {
        IQuestion currentQuestion = questions.get(currentQuestionIndex);

        new BukkitRunnable() {
            int remainingCurrentQuestionTimeToAnswer = currentQuestion.getTimeToAnswer();

            @Override
            public void run() {
                if (state.equals(State.ENDING)) {
                    cancel();
                    return;
                }

                if (remainingCurrentQuestionTimeToAnswer >= 0) {
                    onRoundEachSecond(remainingCurrentQuestionTimeToAnswer);
                    if (remainingCurrentQuestionTimeToAnswer == 0) {
                        cancel();
                    }
                    remainingCurrentQuestionTimeToAnswer--;
                }
            }
        }.runTaskTimer(Plugin.getInstance(), 0L, 20L);  // 20 ticks = 1 second
    }

    private void startDelayTimer() {
        new BukkitRunnable() {
            int remainingDelayTime = roundDelayTime;

            @Override
            public void run() {
                if (state.equals(State.ENDING)) {
                    cancel();
                    return;
                }

                if (remainingDelayTime >= 0) {
                    onRoundDelayEachSecond(remainingDelayTime);
                    if (remainingDelayTime == 0) {
                        cancel();
                    }
                    remainingDelayTime--;
                }
            }
        }.runTaskTimer(Plugin.getInstance(), 0L, 20L);  // 20 ticks = 1 second
    }

    private boolean isFirstRound() {
        return currentQuestionIndex == 0;
    }

    protected void onRoundEachSecond(int secondsLeft) {
        Bukkit.getPluginManager().callEvent(new QuizRoundRunningEvent(Quiz.this, secondsLeft));
        if (secondsLeft == 0) {
            currentQuestionIndex++;
            if (questionLeft()) {
                runNewRound();
            } else {
                end(new QuizNormalEndReason());
            }
        }
    }

    protected void onRoundDelayEachSecond(int secondsLeft) {
        Bukkit.getPluginManager().callEvent(new QuizRoundDelayRunningEvent(Quiz.this, secondsLeft));
        if (secondsLeft == 0) {
            startQuestionTimer();
        }
    }

    public void end(QuizEndingReason quizEndingReason) {
        this.state = State.ENDING;
        new BukkitRunnable() {
            int remainingEndingTime = endingTime;

            @Override
            public void run() {
                if (remainingEndingTime >= 0) {
                    onEachSecondEnding(remainingEndingTime, quizEndingReason);
                    if (remainingEndingTime == 0) {
                        cancel();
                    }
                    remainingEndingTime--;
                }
            }
        }.runTaskTimer(Plugin.getInstance(), 0L, 20L);
    }

    private void onEachSecondEnding(int secondsLeft, QuizEndingReason quizEndingReason) {
        Bukkit.getPluginManager().callEvent(new QuizEndingRunningEvent(this, secondsLeft, quizEndingReason));
    }

    private boolean questionLeft() {
        return currentQuestionIndex < questions.size();
    }

}
