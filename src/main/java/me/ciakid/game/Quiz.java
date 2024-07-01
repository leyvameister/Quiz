package me.ciakid.game;


import me.ciakid.Plugin;
import me.ciakid.context.PlayerContext;
import me.ciakid.event.*;
import me.ciakid.game.model.Arena;
import me.ciakid.game.model.IQuestion;
import me.ciakid.listener.quiz.end.QuizEndingReason;
import me.ciakid.player.QuizPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
    private boolean canceled;
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

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<QuizPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<QuizPlayer> players) {
        this.players = players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    @Override
    public Location getWaitingSpawnpoint() {
        return waitingSpawnpoint;
    }

    public void setWaitingSpawnpoint(Location waitingSpawnpoint) {
        this.waitingSpawnpoint = waitingSpawnpoint;
    }

    @Override
    public Location getArenaSpawnpoint() {
        return arenaSpawnpoint;
    }

    public void setArenaSpawnpoint(Location arenaSpawnpoint) {
        this.arenaSpawnpoint = arenaSpawnpoint;
    }

    public List<IQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<IQuestion> questions) {
        this.questions = questions;
    }

    @Override
    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
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

    public boolean isCanceled() {
        return canceled;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
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
        Bukkit.getPluginManager().callEvent(new QuizStartEvent(this));
    }

    public void beginNewRound() {
        Bukkit.getPluginManager().callEvent(new QuizBeginNewRoundEvent(this));
    }

    public void end(QuizEndingReason quizEndingReason) {
        this.canceled = true;
        Bukkit.getPluginManager().callEvent(new QuizEndEvent(this, quizEndingReason));
    }

}
