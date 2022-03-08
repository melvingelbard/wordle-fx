package wordle.wordlefx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    /**
     * Return an array of @{link Feedback} representing the
     * state of each character in the given word.
     * @param currentWord
     * @return
     */
    // TODO: If there are 2 occurrences of letter, first one might be orange but not second! Fix that
    Feedback[] submit(String currentWord) throws IOException {
        if (currentWord.length() != word.length())
            return new Feedback[5];
        if (currentWord.toLowerCase().equals(word))
            return new Feedback[]{Feedback.CORRECT, Feedback.CORRECT, Feedback.CORRECT, Feedback.CORRECT, Feedback.CORRECT};
        else {
            if (isValid(currentWord)) {
                guesses.add(currentWord);
                Feedback[] out = new Feedback[currentWord.length()];
                for (int c = 0; c < currentWord.length(); c++) {
                    if (currentWord.charAt(c) == word.charAt(c))
                        out[c] = Feedback.CORRECT;
                    else {
                        if (word.contains(currentWord.charAt(c) + ""))
                            out[c] = Feedback.MISPLACED;
                        else
                            out[c] = Feedback.WRONG;
                    }
                }
                return out;
            } else
                throw new IOException("Submitted word is not a recognised word!");
        }
    }

    public List<String> getGuesses() {
        return guesses;
    }

    public void addGuess(String guess) {
        if (guess.length() == word.length())
            guesses.add(guess);
    }

    boolean isValid(String currentWord) {
        return chooser.isValid(currentWord.toLowerCase());
    }
}