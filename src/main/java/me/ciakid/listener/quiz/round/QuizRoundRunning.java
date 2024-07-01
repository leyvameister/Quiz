package me.ciakid.listener.quiz.round;

import me.ciakid.event.QuizRoundRunningEvent;
import me.ciakid.game.Quiz;
import me.ciakid.game.model.Answer;
import me.ciakid.game.model.IQuestion;
import me.ciakid.listener.quiz.end.reason.QuizNormalEndReason;
import me.ciakid.player.QuizPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public class QuizRoundRunning implements Listener {

    @EventHandler
    public void onQuizRoundRunning(QuizRoundRunningEvent e) {
        Quiz quiz = e.getQuiz();
        int remainingDelayTime = e.getRemainingDelayTime();

        List<IQuestion> questions = quiz.getQuestions();
        int currentQuestionIndex = quiz.getCurrentQuestionIndex();
        List<QuizPlayer> players = quiz.getPlayers();

        IQuestion currentQuestion = questions.get(currentQuestionIndex);

        broadcast(players, Component.text("Question: ").append(currentQuestion.getQuestionText()));
        List<Answer> allAnswers = currentQuestion.getAllAnswers();
        for (Answer answer : allAnswers) {
            broadcast(players, answer.getAnswerText().getTextComponent());
        }

        if (remainingDelayTime == 0) {
            broadcast(players, Component.text("Time's up!"));
            currentQuestion.getWrongAnswers().forEach(wrongAnswer -> wrongAnswer.getFloor().destroy());
        }

    }

    private void broadcast(List<QuizPlayer> players, TextComponent text) {
        players.forEach(quizPlayer -> {
            UUID playerUuid = quizPlayer.getPlayer();
            Player player = Bukkit.getPlayer(playerUuid);
            player.sendMessage(text);
        });
    }

}