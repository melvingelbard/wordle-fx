package wordle.wordlefx;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GUI {

    private final static int MUL_FACTOR = 100;
    private final static int SPACING = 10;
    private final Wordle wordle;
    private final int COLUMN_NUMBER;
    private final int ROW_NUMBER;
    private final Canvas canvas;
    private final static Stage stage = new Stage();

    private String currentWord = "";

    public GUI(Wordle wordle) {
        this.wordle = wordle;
        this.COLUMN_NUMBER = wordle.getWord().length();
        this.ROW_NUMBER = wordle.getNAttempts();
        this.canvas = new Canvas(COLUMN_NUMBER * MUL_FACTOR + (SPACING + SPACING*COLUMN_NUMBER), ROW_NUMBER * MUL_FACTOR + (SPACING + SPACING*ROW_NUMBER));
        var root = getPane();
        Scene scene = new Scene(root,800,800);
        stage.setScene(scene);

        // Add key typed pressed/typed
        scene.setOnKeyTyped(e -> {
            if (Character.isLetter(e.getCharacter().charAt(0))) {
                if (currentWord.length() > COLUMN_NUMBER) {
                    e.consume();
                    return;
                }
                currentWord += e.getCharacter().toUpperCase();
                updateCanvas(canvas);
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (e.getCode() == KeyCode.ENTER && currentWord.length() == COLUMN_NUMBER) {
                handleSubmission();
                updateCanvas(canvas);
            } else if (e.getCode() == KeyCode.BACK_SPACE) {
                if (currentWord.length() > 0) {
                    currentWord = currentWord.substring(0, currentWord.length()-1);
                    updateCanvas(canvas);
                }
            }
            e.consume();
        });
    }

    public void start() {
        stage.show();
    }

    BorderPane getPane() {
        BorderPane pane = new BorderPane();
        GridPane topPane = new GridPane();
        BorderPane bottomPane = getBottomPane();

        drawGrid(canvas.getGraphicsContext2D(), COLUMN_NUMBER, ROW_NUMBER);
        topPane.add(canvas, 0, 0);
        topPane.setPadding(new Insets(50, 50, 50, 100));

        pane.setTop(topPane);
        pane.setBottom(bottomPane);

        return pane;
    }

    private BorderPane getBottomPane() {
        BorderPane bottomPane = new BorderPane();
        GridPane correctPane = new GridPane();
        GridPane misplacedPane = new GridPane();
        GridPane wrongPane = new GridPane();

        bottomPane.setLeft(correctPane);
        bottomPane.setCenter(misplacedPane);
        bottomPane.setRight(wrongPane);
        return bottomPane;
    }

    private void handleSubmission() {
        if (currentWord.length() != COLUMN_NUMBER)
            return;
        try {
            Feedback[] feedback = wordle.submit(currentWord);
            if (!isCorrect(feedback)) {
                if (isValid(currentWord))
                    currentWord = "";
            } else
                gameOver();
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        if (wordle.getGuesses().size() == wordle.getNAttempts())
            gameOver();
    }

    private boolean isCorrect(Feedback[] feedback) {
        return Arrays.equals(new Feedback[]{Feedback.CORRECT, Feedback.CORRECT, Feedback.CORRECT, Feedback.CORRECT, Feedback.CORRECT}, feedback);
    }

    private void gameOver() {
        try {
            if (wordle.getGuesses().contains(wordle.getWord()))
                System.out.println("YOU WON!!");
            else
                System.out.println("YOU LOST!!");
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }

    void updateCanvas(Canvas canvas) {
        var g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawGrid(g, COLUMN_NUMBER, ROW_NUMBER);
        int row = 0;
        for (var guess: wordle.getGuesses()) {
            int col = 0;
            for (char c: guess.toCharArray()) {
                Color color = Color.GRAY;
                if (wordle.getWord().contains(String.valueOf(c).toLowerCase())) {
                    color = wordle.getWord().charAt(col) == Character.toLowerCase(c) ? Color.GREEN : Color.DARKORANGE;
                }
                g.setFill(color);
                g.fillRoundRect(1+col*MUL_FACTOR + col*SPACING, 1+row*MUL_FACTOR + row*SPACING, MUL_FACTOR, MUL_FACTOR, 50, 50);

                g.setFill(Color.WHITE);
                g.setFont(new Font(g.getFont().getName(), 75));
                g.fillText(c + "", (col*MUL_FACTOR) + (col++*SPACING) + 29, (row*MUL_FACTOR) + (row*SPACING) + 69);
            }
            row++;
        }

        g.setFill(Color.BLACK);
        int col = 0;
        for (char c: currentWord.toCharArray()) {
            g.setFont(new Font(g.getFont().getName(), 50));
            g.fillText(c + "", (col * MUL_FACTOR) + (col++*SPACING) + 40, (row*MUL_FACTOR) + (row*SPACING) + 66);
        }
    }

    private static void drawGrid(GraphicsContext g, int nCol, int nRows) {
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCol; col++) {
                g.setFill(Color.GRAY);
                g.strokeRoundRect(1+col*MUL_FACTOR + col*SPACING, 1+row*MUL_FACTOR + row*SPACING, MUL_FACTOR, MUL_FACTOR, 50, 50);
            }
        }
    }

    private boolean isValid(String currentWord) {
        return wordle.isValid(currentWord.toLowerCase());
    }

    private Color getColor(Feedback feedback) {
        return switch (feedback) {
            case CORRECT -> Color.GREEN;
            case MISPLACED -> Color.ORANGE;
            case WRONG -> Color.GRAY;
            default -> Color.WHITE;
        };
    }
}

