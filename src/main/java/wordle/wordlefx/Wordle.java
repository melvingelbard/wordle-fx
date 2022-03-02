package wordle.wordlefx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wordle {

    private final WordChooser chooser;
    private final int nAttempts;

    private String word;

    private List<String> guesses = new ArrayList<>();

    Wordle(WordChooser chooser, int nAttempts) {
        this.chooser = chooser;
        this.word = chooser.chooseRandomWord();
        this.nAttempts = nAttempts;
    }

    public String getWord() {
        return word;
    }

    public int getNAttempts() {
        return nAttempts;
    }

    // TODO
    Hint[] submit(String guess) {
        Hint[] out = new Hint[word.length()];
        if (guess.equals(word))
            Arrays.fill(out, Hint.GREEN);

        return out;
    }

    void handleSubmission() {
        if (currentWord.length() != word.length())
            return;
        if (currentWord.equals(word))
            System.out.println("Success!");
        else {
            if (isValid(currentWord)) {
                System.out.println("Valid, but not the right one!");
                guesses.add(currentWord);
                currentWord = "";
            } else
                System.out.println("Non valid!");
        }
    }

    private boolean isValid(String currentWord) {
        return chooser.isValid(currentWord.toLowerCase());
    }


    public void handleBackspace() {
        currentWord = currentWord.isEmpty() ? "" : currentWord.substring(0, currentWord.length() - 1);
    }

    public List<String> getGuesses() {
        return guesses;
    }
}