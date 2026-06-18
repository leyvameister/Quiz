package me.ciakid;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import me.ciakid.arena.WorldEditArenaService;
import me.ciakid.command.QuizCommand;
import me.ciakid.config.PluginConfiguration;
import me.ciakid.game.QuizGameService;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {
    private BukkitCommandManager<CommandSender> commandManager;
    private PluginConfiguration configuration;
    private QuizGameService gameService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.configuration = PluginConfiguration.load(this);
        this.gameService = new QuizGameService(this, configuration, new WorldEditArenaService());
        this.commandManager = BukkitCommandManager.create(this);
        this.commandManager.registerCommand(new QuizCommand(gameService));

        getServer().getPluginManager().registerEvents(gameService, this);
        getLogger().info("Quiz minigame enabled.");
    }

    @Override
    public void onDisable() {
        if (gameService != null) {
            gameService.shutdown();
        }
        commandManager = null;
    }
}
