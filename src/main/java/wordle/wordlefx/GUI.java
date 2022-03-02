package wordle.wordlefx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application {

    private final static int MUL_FACTOR = 100;
    private final static int SPACING = 10;
    private final static String[][] keyboardRows = new String[][]{
            {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"},
            {"a", "s", "d", "f", "g", "h", "j", "k", "l"},
            {"Enter", "z", "x", "c", "v", "b", "n", "m", "Back"}
    };
    private final Wordle wordle;
    private final int COLUMN_NUMBER;
    private final int ROW_NUMBER;
    private static Stage stage;

    private String currentWord = "";

    public GUI(Wordle wordle) {
        this.wordle = wordle;
        this.COLUMN_NUMBER = wordle.getWord().length();
        this.ROW_NUMBER = wordle.getNAttempts();
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        var root = getPane(wordle);
        Scene scene = new Scene(root,800,800);
        stage.setScene(scene);
        stage.show();
    }

    BorderPane getPane(Wordle wordle) {
        BorderPane pane = new BorderPane();
        GridPane topPane = new GridPane();
        GridPane bottomPane = new GridPane();

        Canvas canvas = new Canvas(COLUMN_NUMBER * MUL_FACTOR + (SPACING + SPACING*COLUMN_NUMBER), ROW_NUMBER * MUL_FACTOR + (SPACING + SPACING*ROW_NUMBER));
        drawGrid(canvas.getGraphicsContext2D(), COLUMN_NUMBER, ROW_NUMBER);
        topPane.add(canvas, 0, 0);
        topPane.setPadding(new Insets(50, 50, 50, 100));

        int rowIndex = 0;
        for (String[] row: keyboardRows) {
            int colIndex = 0;
            for (String key: row) {
                var button = new Button(key);
                button.setOnAction(e -> {
                    if (key.equals("Back"))
                        wordle.handleBackspace();
                    else if (key.equals("Enter"))
                        wordle.handleSubmission();
                    else if (currentWord.length() >= COLUMN_NUMBER)
                        return;
                    else
                        currentWord += key.toUpperCase();

                    updateCanvas(canvas);
                });
                bottomPane.add(button, colIndex++, rowIndex);
            }
            rowIndex++;
        }

        pane.setTop(topPane);
        pane.setBottom(bottomPane);

        pane.setOnKeyTyped(e -> {
            if (Character.isLetter(e.getCharacter().charAt(0))) {
                if (currentWord.length() >= COLUMN_NUMBER) {
                    e.consume();
                    return;
                }
                currentWord += e.getCharacter().toUpperCase();
                updateCanvas(canvas);
            }
        });

        pane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (e.getCode() == KeyCode.ENTER && currentWord.length() == COLUMN_NUMBER) {
                wordle.handleSubmission();
                updateCanvas(canvas);
            } else if (e.getCode() == KeyCode.BACK_SPACE) {
                if (currentWord.length() > 0) {
                    currentWord = currentWord.substring(0, currentWord.length()-1);
                    updateCanvas(canvas);
                }
            }
            e.consume();
        });

        return pane;
    }

    void updateCanvas(Canvas canvas) {
        var g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, 500 + SPACING * COLUMN_NUMBER, 500 + SPACING * ROW_NUMBER);

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
}

