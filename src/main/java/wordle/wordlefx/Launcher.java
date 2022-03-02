package wordle.wordlefx;

import javafx.stage.Stage;

import java.io.IOException;

public class Launcher {

    public static void main(String[] args) throws IOException {
        WordChooser wordChooser = new WordChooser("C:/Users/mgelbard/Desktop/corncob_lowercase.txt", 5);
        var wordle = new Wordle(wordChooser, 5);
        GUI gui = new GUI(wordle);
        gui.start(new Stage());
    }
}
