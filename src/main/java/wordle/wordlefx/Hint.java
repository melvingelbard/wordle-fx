package wordle.wordlefx;

import javafx.scene.paint.Color;

public enum Hint {
    GREEN(Color.WHITE, Color.GREEN),
    YELLOW(Color.WHITE, Color.YELLOW),
    GRAY(Color.WHITE, Color.GRAY),
    NONE(Color.BLACK, Color.WHITE);

    Color fontColor;
    Color backgroundColor;

    Hint(Color fontColor, Color backgroundColor) {
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
    }
}
