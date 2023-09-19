import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.ListIterator;

/**
 * Reads a text file line by line to extract and normalize all words
 * @author Joshua Shew
 */
public class Tokenizer {
    /* stores the list of as provided in the array or read from the txt file */
    private ArrayList<String> wordList;

    /**
     * Read a txt file to extract and normalize all words
     * @param path the path of the txt file to read
     */
    public Tokenizer(String path) throws Exception{
        this.wordList = new ArrayList<String>();

        Scanner scanner;
        try {
            scanner = new Scanner(new File(path));
        }
        catch (Exception e) {
            throw new Exception("File could not be found");
        }

        while (scanner.hasNextLine()) {
            for (String word : scanner.nextLine().split(" "))
                    wordList.add(normalizeWord(word));
        }

        cleanWordList();
    }

    /**
     * Normalizes a list of words
     * @param words
     */
    public Tokenizer(String[] words) {
        this.wordList = new ArrayList<String>();

        for (String word : words) {
            wordList.add(normalizeWord(word));
        }

        cleanWordList();
    }

    /**
     * Provides the processed list of words
     * @return the list of words
     */
    ArrayList<String> wordList() {
        return this.wordList;
    }

    /**
     * Normalizes words by converting to lower case, removing spaces, and removing punctuation
     * @param word the word to normalize
     * @return the normalized word
     */
    public static String normalizeWord(String word) {
        return word.toLowerCase().replaceAll(" ", "").replaceAll("\\p{Punct}", "").replaceAll("“", "");
    }

    /**
     * Cleans up the wordList by removing empty strings
     */
    private void cleanWordList() {
        ListIterator<String> iterator = this.wordList.listIterator();

        while (iterator.hasNext()) {
            if (iterator.next().equals(""))
                iterator.remove();
        }
    }
}
