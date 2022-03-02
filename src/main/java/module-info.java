module wordlefx {
    requires javafx.controls;
    requires javafx.fxml;


    opens wordle.wordlefx to javafx.fxml;
    exports wordle.wordlefx;
}