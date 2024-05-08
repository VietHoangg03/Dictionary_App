package main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {
    @FXML
    private Label questionLabel;

    @FXML
    private TextField answerField;
    @FXML
    private Label correctCountLabel;

    @FXML
    private Label incorrectCountLabel;
    private Random random = new Random();
    private int correctCount = 0;
    private int incorrectCount = 0;


    private List<String> vietnameseWords = new ArrayList<>();
    private List<String> englishWords = new ArrayList<>();
    private int currentQuestionIndex = -1;

    public void initialize() {
        loadWordsFromFile("src/resource/Game/game.txt");
        showNextQuestion();
        correctCountLabel.setText(Integer.toString(correctCount));
        incorrectCountLabel.setText(Integer.toString(incorrectCount));
        congratulationLabel.setVisible(false);
    }

    private void loadWordsFromFile(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\.");
                if (parts.length == 2) {
                    vietnameseWords.add(parts[0].trim());
                    englishWords.add(parts[1].trim());
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showNextQuestion() {
        if (vietnameseWords.isEmpty()) {
            questionLabel.setText("No words available!");
            return;
        }
        currentQuestionIndex = random.nextInt(vietnameseWords.size());
        String word = vietnameseWords.get(currentQuestionIndex);
        questionLabel.setText("What is the English meaning of '" + word + "'?");
        answerField.clear();
    }

    @FXML
    private void onAnswerKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            checkAnswer();
        }
    }

    @FXML
    private void checkKey() {
        String userAnswer = answerField.getText().trim();
        String correctAnswer = englishWords.get(currentQuestionIndex);
        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            questionLabel.setText("Correct!");
            correctCount++;
            if (correctCount % 10 == 0 && correctCount <= 100) {
                showCongratulationMessage(correctCount);
            }
        } else {
            questionLabel.setText("Incorrect! The correct answer is: " + correctAnswer);
            incorrectCount++;
        }
        correctCountLabel.setText(Integer.toString(correctCount));
        incorrectCountLabel.setText(Integer.toString(incorrectCount));
    }

    private void checkAnswer() {
        String userAnswer = answerField.getText().trim();
        String correctAnswer = englishWords.get(currentQuestionIndex);
        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            questionLabel.setText("Correct!");
            correctCount++;
            if (correctCount % 10 == 0 && correctCount <= 100) {
                showCongratulationMessage(correctCount);
            }
        } else {
            questionLabel.setText("Incorrect! The correct answer is: " + correctAnswer);
            incorrectCount++;
        }
        correctCountLabel.setText(Integer.toString(correctCount));
        incorrectCountLabel.setText(Integer.toString(incorrectCount));
    }


    @FXML
    private Label congratulationLabel;

    private void showCongratulationMessage(int correctCount) {
        congratulationLabel.setText("Congratulations! You have reached " + correctCount + " correct answers!");
        congratulationLabel.setVisible(true);
    }
}
