package wordle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class WordChooser {

    private final List<String> words;
    private String chosenWord;

    /**
     * Create a word chooser based on the path of word list
     * @param path
     */
    public WordChooser(String path, int length) throws IOException {
        words = Files.lines(Paths.get(path)).filter(word -> word.length() == length).toList();
    }

    String chooseRandomWord() {
        chosenWord = words.get(new Random().nextInt(words.size())).toLowerCase();
        return chosenWord;
    }

    public List<String> getWords() {
        return words;
    }
}
