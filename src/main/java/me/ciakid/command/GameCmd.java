package me.ciakid.command;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import me.ciakid.GameFinder;
import me.ciakid.Plugin;
import me.ciakid.context.PlayerContext;
import me.ciakid.exception.NoArenasAvailableException;
import me.ciakid.game.Game;
import me.ciakid.game.Quiz;
import me.ciakid.manager.QuizManager;
import me.ciakid.player.QuizPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Command("game")
public class GameCmd extends BaseCommand {
    private final GameFinder gameFinder;

    public GameCmd(GameFinder gameFinder) {
        this.gameFinder = gameFinder;
    }

    @SubCommand("join")
    public void onJoinCommand(CommandSender sender) {
        Player player = (Player) sender;
        QuizPlayer quizPlayer = QuizPlayer.wrap(player);

        if (quizPlayer.getContext().equals(PlayerContext.PLAYING)) {
            player.sendMessage("Error already in game");
            return;
        }
        try {
            Game game = gameFinder.findAvailable();
            game.addPlayer(quizPlayer);
        } catch (NoArenasAvailableException e) {
            player.sendMessage("Couldn't find any available arenas, try again later");
        }
    }

    @SubCommand("leave")
    public void onLeaveCommand(CommandSender sender) {
        Player player = (Player) sender;
        QuizPlayer quizPlayer = QuizPlayer.wrap(player);

        if (quizPlayer.getContext().equals(PlayerContext.NOT_PLAYING)) {
            player.sendMessage(Component.text("You are not in game"));
            return;
        }

        Quiz game = quizPlayer.getQuiz();
        game.removePlayer(quizPlayer);
        player.sendMessage(Component.text("You have left"));
    }

    @SubCommand("test")
    public void onTestCommand(CommandSender sender) {
        QuizManager quizManager = Plugin.getInstance().getQuizManager();
        List<Quiz> liveQuizzes = quizManager.getLiveQuizzes();
        Bukkit.broadcastMessage(String.valueOf(liveQuizzes));
    }

}