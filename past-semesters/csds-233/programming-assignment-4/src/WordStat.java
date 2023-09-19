import java.util.ArrayList;
import java.lang.IllegalArgumentException;

public class WordStat {
    /* stores the list of words from the input */
    private ArrayList<String> wordList;

    /* stores a table with words and their frequency */
    public WordTable wordTable;

    /* stores an ordered list of the entries */
    private ArrayList<HashEntry> entryList;

    /* stores a table with all word pairings */
    public HashTable pairTable;

    /* stores an ordered list of the pair entries */
    private ArrayList<HashEntry> pairList;

    /**
     * Processes the file input
     * @param path the file to process and calculate statistics for
     * @throws Exception if the file cannot be found
     */
    public WordStat(String path) throws Exception {
        this.wordList = (new Tokenizer(path)).wordList();
        processWordList();
    }

    /**
     * Processes the String inputs
     * @param words the words to process
     */
    public WordStat(String[] words) {
        this.wordList = (new Tokenizer(words)).wordList();
        processWordList();
    }

    /**
     * Returns the number of occurences for a word in a tet
     * @param word the word the count
     * @return the number of occurences
     */
    public int wordCount(String word) {
        word = Tokenizer.normalizeWord(word);
        return (this.wordTable.get(word) == -1) ? 0 : this.wordTable.get(word);
    }

    /**
     * Returns the number of occurences for a pair of words in the inputted order
     * @param w1 the first word
     * @param w2 the second word
     * @return the number of occurences
     */
    public int wordPairCount(String w1, String w2) {
        w1 = Tokenizer.normalizeWord(w1);
        w2 = Tokenizer.normalizeWord(w2);
        int num = this.pairTable.get(w1 + " " + w2);
        return (num == -1 ? 0 : num);
    }

    /**
     * Returns the rank of a word
     * @param word the word
     * @return the rank of the word
     */
    public int wordRank(String word) {
        word = Tokenizer.normalizeWord(word);
        int rank = this.wordTable.getRank(word);
        return rank == -1 ? 0 : rank;
    }

    /**
     * Returns the rank of a pair of words
     * @param w1 the first word
     * @param w2 the second word
     * @return the rank of the pair of words
     */
    public int wordPairRank(String w1, String w2) {
        w1 = Tokenizer.normalizeWord(w1);
        w2 = Tokenizer.normalizeWord(w2);
        int rank = this.pairTable.getRank(w1 + " " + w2);
        return (rank == -1) ? 0 : rank;
    }
 
    /**
     * The most common words in the text
     * @param k the number of words to provide
     * @return a list of the most common words
     */
    public String[] mostCommonWords(int k) {
        String[] list = new String[k];

        for (int i = 0; i < k; i++) {
            list[i] = entryList.get(entryList.size() - i - 1).getKey();
        }

        return list;
    }

    /**
     * The least common words in the text
     * @param k the number of words to provide
     * @return a list of the least common words
     */
    public String[] leastCommonWords(int k) {
        String[] list = new String[k];
        
        for (int i = 0; i < k; i++) {
            list[i] = entryList.get(i).getKey();
        }

        return list;
    }

    /**
     * The most common word pairs in the text
     * @param k the number of word pairs to provide
     * @return a list of the most common word pairs
     */
    public String[] mostCommonWordPairs(int k) {
        String[] list = new String[k];

        for (int i = 0; i < k; i++) {
            list[i] = pairList.get(pairList.size() - i - 1).getKey();
        }

        return list;
    }

    /**
     * The most common words before and after a input word
     * @param k the number of words to list
     * @param baseWord the word to look before or after
     * @param i 1 or after, -1 for before, any other input is not allowed
     * @return a list of the most common words before or after
     */
    public String[] mostCommonCollocs(int k, String baseWord, int i) throws IllegalArgumentException {
        if (i != -1 && i != 1) {
            throw new IllegalArgumentException();
        }

        baseWord = Tokenizer.normalizeWord(baseWord);

        /* chooses the correct list */
        ArrayList<HashEntry> mainList;
        if (i == -1) {
            mainList = wordTable.getEntry(baseWord).getWordPrecedingList();
        }
        else {
            mainList = wordTable.getEntry(baseWord).getWordFollowingList();
        }

        /* find the most common collocations */
        String[] list = new String[Math.min(k, mainList.size())];
        for (int j = 0; j < k && j < mainList.size(); j++) {
            list[j] = mainList.get(mainList.size() - j - 1).getKey();
        }

        return list;
    }

    /**
     * Processes the inputted words
     */
    private void processWordList() {
        /* add all words to the hash table */
        this.wordTable = new WordTable(this.wordList);
        for (String word : this.wordList) {
            this.wordTable.update(word, (this.wordTable.get(word) == -1 ?
                                         1 : this.wordTable.get(word) + 1));
        }

        /* add all word pairs to the hash table */
        this.pairTable = new HashTable();
        for (int i = 0; i < this.wordList.size() - 1; i++) {
            String pair = this.wordList.get(i) + " " + this.wordList.get(i + 1);
            this.pairTable.update(pair, (this.pairTable.get(pair) == -1 ?
                                         1 : this.pairTable.get(pair) + 1));
        }

        /* sorts the lists */
        this.entryList = this.wordTable.exportArray();
        this.entryList.sort(new HashEntry.ValueCompare());
        this.pairList = this.pairTable.exportArray();
        this.pairList.sort(new HashEntry.ValueCompare());

        /* calculates rankings */
        for (int i = 0; i < entryList.size(); i++) {
            wordTable.updateRank(entryList.get(i).getKey(), entryList.size() - i);
        }
        for (int i = 0; i < pairList.size(); i++) {
            pairTable.updateRank(pairList.get(i).getKey(), pairList.size() - i);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("WordStat frankenstein = new WordStat(\"/Users/Joshua/Documents/_CASE/CSDS_233/P4/Project4/src/frankenstein.txt\");");
        WordStat frankenstein = new WordStat("/Users/Joshua/Documents/_CASE/CSDS_233/P4/Project4/src/frankenstein.txt");

        System.out.println("DEMONSTRATION WITH FRANKENSTEIN");
        System.out.println("");

        System.out.println("System.out.println(frankenstein.wordCount(\"and\"));");
        System.out.println(frankenstein.wordCount("and"));

        System.out.println("");

        System.out.println("System.out.println(frankenstein.wordPairCount(\"and\", \"the\"));");
        System.out.println(frankenstein.wordPairCount("and", "the"));

        System.out.println("");

        System.out.println("System.out.println(frankenstein.wordRank(\"Frankenstein\"));");
        System.out.println(frankenstein.wordRank("Frankenstein"));

        System.out.println("");

        System.out.println("System.out.println(frankenstein.wordPairRank(\"so\", \"he\"));");
        System.out.println(frankenstein.wordPairRank("so", "he"));

        System.out.println("");

        System.out.println("String[] s1 = frankenstein.mostCommonWords(20);");
        System.out.println("// print list s1 nicely");
        System.out.print("[ ");
        for (String s : frankenstein.mostCommonWords(20))
            System.out.print(s + " ");
        System.out.println("]");

        System.out.println("");

        System.out.println("String[] s1 = frankenstein.leastCommonWords(20);");
        System.out.println("// print list s1 nicely");
        System.out.print("[ ");
        for (String s : frankenstein.leastCommonWords(20))
            System.out.print(s + " ");
        System.out.println("]");

        System.out.println("");

        System.out.println("String[] s1 = frankenstein.mostCommonWordPairs(20);");
        System.out.println("// print list s1 nicely");
        System.out.print("[ ");
        for (String s : frankenstein.mostCommonWordPairs(20))
            System.out.print(s + " ");
        System.out.println("]");

        System.out.println("");

        System.out.println("String[] s1 = frankenstein.mostCommonCollocs(20, \"Frankenstein\", 1).toString());");
        System.out.println("// print list s1 nicely");
        System.out.print("[ ");
        for (String s : frankenstein.mostCommonCollocs(20, "Frankenstein", 1))
            System.out.print(s + " ");
        System.out.println("]");

        System.out.println("");

        System.out.println("String[] s1 = frankenstein.mostCommonCollocs(20, \"Frankenstein\", 01).toString());");
        System.out.println("// print list s1 nicely");
        System.out.print("[ ");
        for (String s : frankenstein.mostCommonCollocs(20, "Frankenstein", -1))
            System.out.print(s + " ");
        System.out.println("]");

        System.out.println("");
        System.out.println("** Demonstration Done **");
    }
}
