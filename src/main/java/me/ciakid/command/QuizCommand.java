package me.ciakid.command;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import me.ciakid.game.QuizGameService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("quiz")
public final class QuizCommand extends BaseCommand {
    private final QuizGameService gameService;

    public QuizCommand(QuizGameService gameService) {
        this.gameService = gameService;
    }

    @SubCommand("join")
    public void join(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Solo jugadores pueden entrar al quiz.", NamedTextColor.RED));
            return;
        }
        gameService.join(player);
    }

    @SubCommand("leave")
    public void leave(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Solo jugadores pueden salir del quiz.", NamedTextColor.RED));
            return;
        }
        gameService.leave(player);
    }

    @SubCommand("status")
    public void status(CommandSender sender) {
        sender.sendMessage(Component.text("Partidas activas: " + gameService.activeGames(), NamedTextColor.AQUA));
    }
}
