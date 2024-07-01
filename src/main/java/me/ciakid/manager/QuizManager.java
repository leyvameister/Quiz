package me.ciakid.manager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.ciakid.Plugin;
import me.ciakid.exception.NoArenasAvailableException;
import me.ciakid.util.ColorUtil;
import me.ciakid.component.ColoredString;
import me.ciakid.game.Quiz;
import me.ciakid.game.model.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class QuizManager {
    private final List<Quiz> liveQuizs = new ArrayList<>();
    private final FileConfiguration questionsData;
    private final FileConfiguration arenasData;

    public QuizManager(File questionsFile, File arenasFile) {
        this.questionsData = YamlConfiguration.loadConfiguration(questionsFile);
        this.arenasData = YamlConfiguration.loadConfiguration(arenasFile);
    }

    public Quiz createNewQuiz() throws NoArenasAvailableException {
        Random random = new Random();

        // Get lists of questions and arenas
        List<Map<String, Object>> questionsList = getQuestionsList();
        List<Map<String, Object>> arenasList = getArenasList();

        // Select a random arena
        Map<String, Object> selectedArena = selectRandomArena(arenasList, random);

        // Read arena details

        String name = (String) selectedArena.get("name");
        World world = Bukkit.getWorld((String) selectedArena.get("world"));
        com.sk89q.worldedit.world.World worldeditWorld = BukkitAdapter.adapt(world);
        int maxPlayers = (int) selectedArena.get("max_players");
        int minPlayers = (int) selectedArena.get("min_players");
        int rounds = (int) selectedArena.get("rounds");

        Location waitingSpawnpoint = createLocationFromMap((Map<String, Object>) selectedArena.get("waiting_location"), world);
        Location arenaSpawnpoint = createLocationFromMap((Map<String, Object>) selectedArena.get("arena_spawnpoint"), world);
        Map<String, Object> arenaRegionMap = (Map<String, Object>) selectedArena.get("arena_region");

        QuizArena quizArena = createQuizArena(arenaRegionMap, worldeditWorld);

        // Read floors details
        List<ColoredQuizFloor> floorList = createFloorList(selectedArena, worldeditWorld);

        // Select random questions
        List<IQuestion> selectedQuestions = selectRandomQuestions(questionsList, rounds, floorList, random);

        // Placeholder times
        int countdownTime = 5;
        int roundDelayTime = 3;
        int endingTime = 5;

        Quiz quiz = new Quiz(name, maxPlayers, minPlayers, waitingSpawnpoint, arenaSpawnpoint, selectedQuestions, quizArena, countdownTime, roundDelayTime, endingTime);
        liveQuizs.add(quiz);

        return quiz;
    }

    public List<Quiz> getLiveQuizzes() {
        return liveQuizs;
    }

    public void unregister(Quiz quiz) {
        liveQuizs.remove(quiz);
    }

    private List<Map<String, Object>> getQuestionsList() {
        return (List<Map<String, Object>>) questionsData.get("questions");
    }

    private List<Map<String, Object>> getArenasList() {
        return (List<Map<String, Object>>) arenasData.get("arenas");
    }

    private Map<String, Object> selectRandomArena(List<Map<String, Object>> arenasList, Random random) throws NoArenasAvailableException {
        int attempts = 0;
        while (attempts < arenasList.size()) {

            Map<String, Object> arenaMap = (Map<String, Object>) arenasList.get(attempts).get("arena");
            String arenaName = (String) arenaMap.get("name");

            boolean isArenaInUse = false;
            for (Quiz quiz : liveQuizs) {
                if (quiz.getName().equals(arenaName)) {
                    isArenaInUse = true;
                    break;
                }
            }

            if (!isArenaInUse) {
                return arenaMap;
            }

            attempts++;
        }
        throw new NoArenasAvailableException("No available arenas after checking all.");
    }

//    public Map<String, Object> selectRandomArena(List<Map<String, Object>> arenasList, Random random) throws NoArenasAvailableException {
//        int attempts = 0;
//        while (attempts < arenasList.size()) {
//
//            Map<String, Object> arenaMap = (Map<String, Object>) arenasList.get(random.nextInt(arenasList.size())).get("arena");
//            String arenaName = (String) arenaMap.get("name");
//
//            boolean isArenaInUse = false;
//            for (Quiz quiz : liveQuizs) {
//                if (quiz.getName().equals(arenaName)) {
//                    isArenaInUse = true;
//                    break;
//                }
//            }
//
//            if (!isArenaInUse) {
//                return arenaMap;
//            }
//
//            attempts++;
//        }
//        throw new NoArenasAvailableException("No available arenas after checking all.");
//    }

    private List<ColoredQuizFloor> createFloorList(Map<String, Object> selectedArena, com.sk89q.worldedit.world.World world) {
        return Arrays.asList(createQuizFloor((Map<String, Object>) selectedArena.get("first_floor"), world), createQuizFloor((Map<String, Object>) selectedArena.get("second_floor"), world), createQuizFloor((Map<String, Object>) selectedArena.get("third_floor"), world), createQuizFloor((Map<String, Object>) selectedArena.get("fourth_floor"), world));
    }

    private List<IQuestion> selectRandomQuestions(List<Map<String, Object>> questionsList, int rounds, List<ColoredQuizFloor> floorList, Random random) {
        List<IQuestion> selectedQuestions = new ArrayList<>();
        for (int i = 0; i < rounds; i++) {
            Map<String, Object> questionMap = questionsList.get(random.nextInt(questionsList.size()));
            selectedQuestions.add(createQuestionFromMap(questionMap, floorList, random));
        }
        return selectedQuestions;
    }

    private Location createLocationFromMap(Map<String, Object> locationMap, World world) {
        return new Location(world, (int) locationMap.get("x"), (int) locationMap.get("y"), (int) locationMap.get("z"));
    }

    private QuizArena createQuizArena(Map<String, Object> arenaRegionMap, com.sk89q.worldedit.world.World world) {
        List<Integer> minList = (List<Integer>) arenaRegionMap.get("min");
        List<Integer> maxList = (List<Integer>) arenaRegionMap.get("max");
        BlockVector3 min = BlockVector3.at(minList.get(0), minList.get(1), minList.get(2));
        BlockVector3 max = BlockVector3.at(maxList.get(0), maxList.get(1), maxList.get(2));

        return new QuizArena(new CuboidRegion(world, min, max));
    }

    private ColoredQuizFloor createQuizFloor(Map<String, Object> floorMap, com.sk89q.worldedit.world.World world) {
        List<Integer> minList = (List<Integer>) floorMap.get("min");
        List<Integer> maxList = (List<Integer>) floorMap.get("max");
        NamedTextColor color = ColorUtil.getNamedTextColor((String) floorMap.get("color"));
        BlockVector3 min = BlockVector3.at(minList.get(0), minList.get(1), minList.get(2));
        BlockVector3 max = BlockVector3.at(maxList.get(0), maxList.get(1), maxList.get(2));
        return new ColoredQuizFloor(world, min, max, color);
    }

    private IQuestion createQuestionFromMap(Map<String, Object> questionMap, List<ColoredQuizFloor> floorList, Random random) {
        TextComponent questionText = Component.text((String) questionMap.get("question"));
        List<ColoredQuizFloor> shuffledFloors = new ArrayList<>(floorList);
        Collections.shuffle(shuffledFloors);

        Map<String, String> correctAnswerMap = (Map<String, String>) questionMap.get("correct_answer");
        ColoredString correctAnswerString = new ColoredString(correctAnswerMap.get("text"), shuffledFloors.get(0).getColor());
        Answer correctAnswer = new Answer(correctAnswerString, shuffledFloors.remove(0));

        List<Map<String, String>> wrongAnswersList = (List<Map<String, String>>) questionMap.get("wrong_answers");
        List<Answer> wrongAnswers = new ArrayList<>();
        for (Map<String, String> wrongAnswerMap : wrongAnswersList) {
            ColoredString wrongAnswerString = new ColoredString(wrongAnswerMap.get("text"), shuffledFloors.get(0).getColor());
            wrongAnswers.add(new Answer(wrongAnswerString, shuffledFloors.remove(0)));
        }

        int timeToAnswer = (int) questionMap.get("time_to_answer");
        return new Question(questionText, correctAnswer, wrongAnswers, timeToAnswer);
    }

}