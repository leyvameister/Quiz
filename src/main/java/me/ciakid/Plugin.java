package me.ciakid;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import me.ciakid.command.GameCmd;
import me.ciakid.listener.quiz.*;
import me.ciakid.listener.quiz.end.QuizEndingRunning;
import me.ciakid.listener.quiz.round.QuizRoundDelayRunning;
import me.ciakid.listener.quiz.round.QuizRoundRunning;
import me.ciakid.listener.quiz.start.QuizCountdownRunning;
import me.ciakid.manager.QuizManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public final class Plugin extends JavaPlugin {
    private QuizManager quizManager;

    public static Plugin getInstance() {
        return getPlugin(Plugin.class);
    }

    @Override
    public void onEnable() {
        BukkitCommandManager<CommandSender> manager = BukkitCommandManager.create(this);

        GameCmd quizGameCmd = new GameCmd(new QuizFinder());

        manager.registerCommand(quizGameCmd);

        registerListeners();

        File questionsFile = createQuestionsFileIfNotExists();
        File arenasFile = createArenasFileIfNotExists();

        this.quizManager = new QuizManager(questionsFile, arenasFile);
    }

    public QuizManager getQuizManager() {
        return quizManager;
    }

    public Location getLobby() {
        return new Location(Bukkit.getWorld("world"), -17, 72, -147);
    }

    private File createQuestionsFileIfNotExists() {
        File questionsFile = new File(this.getDataFolder(), "Questions.yml");

        if (!questionsFile.exists()) {
            getLogger().warning("El archivo questions.yml no existe. Creando uno nuevo...");
            this.saveResource("Questions.yml", false);
        }
        return questionsFile;
    }

    private File createArenasFileIfNotExists() {
        File arenasFile = new File(this.getDataFolder(), "Arenas.yml");
        if (!arenasFile.exists()) {
            getLogger().warning("El archivo arenas.yml no existe. Creando uno nuevo...");
            this.saveResource("Arenas.yml", false);
        }
        return arenasFile;
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new QuizPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new QuizPlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new QuizPlayerLose(), this);
        getServer().getPluginManager().registerEvents(new QuizCountdownRunning(), this);
        getServer().getPluginManager().registerEvents(new QuizRoundDelayRunning(), this);
        getServer().getPluginManager().registerEvents(new QuizRoundRunning(), this);
        getServer().getPluginManager().registerEvents(new QuizEndingRunning(), this);
    }

}