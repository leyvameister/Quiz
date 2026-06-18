package me.ciakid.config;

import me.ciakid.model.ArenaDefinition;
import me.ciakid.model.FloorDefinition;
import me.ciakid.model.QuestionDefinition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class PluginConfiguration {
    private final Location lobbyLocation;
    private final int countdownSeconds;
    private final int intermissionSeconds;
    private final int endingSeconds;
    private final List<ArenaDefinition> arenas;
    private final List<QuestionDefinition> questions;

    private PluginConfiguration(
            Location lobbyLocation,
            int countdownSeconds,
            int intermissionSeconds,
            int endingSeconds,
            List<ArenaDefinition> arenas,
            List<QuestionDefinition> questions
    ) {
        this.lobbyLocation = lobbyLocation;
        this.countdownSeconds = countdownSeconds;
        this.intermissionSeconds = intermissionSeconds;
        this.endingSeconds = endingSeconds;
        this.arenas = List.copyOf(arenas);
        this.questions = List.copyOf(questions);
    }

    public static PluginConfiguration load(JavaPlugin plugin) {
        plugin.reloadConfig();
        ConfigurationSection config = plugin.getConfig();
        Location lobby = readLocation(requiredSection(config, "lobby"));
        int countdown = config.getInt("timers.countdown", 10);
        int intermission = config.getInt("timers.intermission", 4);
        int ending = config.getInt("timers.ending", 8);
        List<ArenaDefinition> arenas = readArenas(requiredSection(config, "arenas"));
        List<QuestionDefinition> questions = readQuestions(requiredSection(config, "questions"));

        if (arenas.isEmpty()) {
            throw new IllegalStateException("At least one arena must be configured.");
        }
        if (questions.isEmpty()) {
            throw new IllegalStateException("At least one question must be configured.");
        }

        return new PluginConfiguration(lobby, countdown, intermission, ending, arenas, questions);
    }

    public Location lobbyLocation() {
        return lobbyLocation.clone();
    }

    public int countdownSeconds() {
        return countdownSeconds;
    }

    public int intermissionSeconds() {
        return intermissionSeconds;
    }

    public int endingSeconds() {
        return endingSeconds;
    }

    public List<ArenaDefinition> arenas() {
        return arenas;
    }

    public List<QuestionDefinition> questions() {
        return questions;
    }

    private static List<ArenaDefinition> readArenas(ConfigurationSection arenasSection) {
        List<ArenaDefinition> arenas = new ArrayList<>();
        for (String arenaId : arenasSection.getKeys(false)) {
            ConfigurationSection section = requiredSection(arenasSection, arenaId);
            arenas.add(new ArenaDefinition(
                    arenaId,
                    section.getInt("min-players", 2),
                    section.getInt("max-players", 10),
                    section.getInt("rounds", 5),
                    readLocation(requiredSection(section, "waiting-spawn")),
                    readLocation(requiredSection(section, "game-spawn")),
                    readCuboid(requiredSection(section, "region")),
                    readFloors(requiredSection(section, "floors"))
            ));
        }
        return arenas;
    }

    private static List<FloorDefinition> readFloors(ConfigurationSection floorsSection) {
        List<FloorDefinition> floors = new ArrayList<>();
        for (String floorId : floorsSection.getKeys(false)) {
            ConfigurationSection section = requiredSection(floorsSection, floorId);
            floors.add(new FloorDefinition(floorId, section.getString("label", floorId), readCuboid(section)));
        }
        if (floors.size() < 2) {
            throw new IllegalStateException("Each arena must define at least two answer floors.");
        }
        return floors;
    }

    private static List<QuestionDefinition> readQuestions(ConfigurationSection questionsSection) {
        List<QuestionDefinition> questions = new ArrayList<>();
        for (String questionId : questionsSection.getKeys(false)) {
            ConfigurationSection section = requiredSection(questionsSection, questionId);
            String prompt = Objects.requireNonNull(section.getString("prompt"), "Missing question prompt: " + questionId);
            String correctAnswer = Objects.requireNonNull(section.getString("correct-answer"), "Missing correct answer: " + questionId);
            List<String> wrongAnswers = section.getStringList("wrong-answers");
            int seconds = section.getInt("seconds", 8);
            if (wrongAnswers.isEmpty()) {
                throw new IllegalStateException("Question " + questionId + " needs at least one wrong answer.");
            }
            questions.add(new QuestionDefinition(prompt, correctAnswer, wrongAnswers, seconds));
        }
        return questions;
    }

    private static CuboidBounds readCuboid(ConfigurationSection section) {
        return new CuboidBounds(
                section.getInt("min.x"), section.getInt("min.y"), section.getInt("min.z"),
                section.getInt("max.x"), section.getInt("max.y"), section.getInt("max.z")
        );
    }

    private static Location readLocation(ConfigurationSection section) {
        String worldName = Objects.requireNonNull(section.getString("world"), "Missing world name in location");
        World world = Objects.requireNonNull(org.bukkit.Bukkit.getWorld(worldName), "World not found: " + worldName);
        return new Location(world, section.getDouble("x"), section.getDouble("y"), section.getDouble("z"),
                (float) section.getDouble("yaw", 0), (float) section.getDouble("pitch", 0));
    }

    private static ConfigurationSection requiredSection(ConfigurationSection parent, String path) {
        ConfigurationSection section = parent.getConfigurationSection(path);
        if (section == null) {
            throw new IllegalStateException("Missing configuration section: " + path);
        }
        return section;
    }
}
