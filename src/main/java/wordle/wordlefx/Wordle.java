package wordle.wordlefx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Wordle {

    private final WordChooser chooser;
    private final List<String> words;
    private final int nAttempts;
    private final String word;

    private final List<String> guesses = new ArrayList<>();

    public Wordle(WordChooser chooser, int nAttempts) {
        this.chooser = chooser;
        this.words = chooser.getWords();
        this.word = chooser.chooseRandomWord();
        this.nAttempts = nAttempts;
    }

    public Wordle(String word, List<String> words, int nAttempts) {
        this.chooser = null;
        this.words = words;
        this.word = word;
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
     * @param currentWord the word to submit
     * @return feedback array (one Feedback per character)
     */
    // TODO: If there are 2 occurrences of letter, first one might be orange but not second! Fix that
    public Feedback[] submit(String currentWord) throws IOException {
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

    boolean isValid(String currentWord) {
        return words.contains(currentWord.toLowerCase());
    }
}