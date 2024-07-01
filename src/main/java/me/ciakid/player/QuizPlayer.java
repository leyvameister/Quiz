package me.ciakid.player;

import me.ciakid.context.PlayerContext;
import me.ciakid.game.Quiz;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuizPlayer implements GamePlayer {
    private static final Map<UUID, QuizPlayer> players = new HashMap<>();

    private final UUID player;
    private ItemStack[] savedInventory;
    private PlayerContext context;
    private Quiz quiz;
    private GameMode savedGameMode;


    public QuizPlayer(UUID player) {
        this.player = player;
    }

    @Override
    public UUID getPlayer() {
        return player;
    }

    @Override
    public ItemStack[] getSavedInventory() {
        return savedInventory;
    }

    @Override
    public void saveInventory() {
        this.savedInventory = Bukkit.getPlayer(player).getInventory().getContents();
    }

    @Override
    public PlayerContext getContext() {
        return context;
    }

    @Override
    public void setContext(PlayerContext context) {
        this.context = context;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Override
    public GameMode getSavedGameMode() {
        return savedGameMode;
    }

    @Override
    public void saveGameMode() {
        this.savedGameMode = Bukkit.getPlayer(player).getGameMode();
    }

    public void destroy() {
        players.remove(player);
    }

    public static QuizPlayer wrap(Player player) {
        if (players.containsKey(player.getUniqueId())) {
            return players.get(player.getUniqueId());
        } else {
            QuizPlayer quizPlayer = new QuizPlayer(player.getUniqueId());
            players.put(player.getUniqueId(), quizPlayer);
            return quizPlayer;
        }
    }

}
