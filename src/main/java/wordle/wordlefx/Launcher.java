package wordle.wordlefx;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        WordChooser wordChooser = new WordChooser("C:/Users/mel/Desktop/corncob_lowercase.txt", 5);
        var wordle = new Wordle(wordChooser, 6);
        GUI gui = new GUI(wordle);
        gui.start();
    }
}
