import java.util.ArrayList;

public class WordEntry extends HashEntry {
    /* stores all words that follow this word and their frequencies */
    private HashTable wordFollowingTable;

    /* stores all words that precede this word and their frequencies */
    private HashTable wordPrecedingTable;

    /* stores all entries in the wordFollowingTable */
    private ArrayList<HashEntry> wordFollowingList;

    /* stores all entries in the wordPrecedingList */
    private ArrayList<HashEntry> wordPrecedingList;

    /**
     * Creates a new word entry based on the word and a master list of all word being processed by WordStat
     * @param key the key of the entry
     * @param value the value of the entry
     * @param wordList the list of all words
     */
    public WordEntry(String key, int value, ArrayList<String> wordList) {
        super(key, value);

        this.wordFollowingTable = new HashTable();
        this.wordPrecedingTable = new HashTable();

        process(wordList);
    }

    /**
     * Getter method for wordFollowingTable
     * @return the wordFollowingTable
     */
    public HashTable getWordFollowingTable() {
        return this.wordFollowingTable;
    }

    /**
     * Getter method for wordPrecedingTable
     * @return the wordPrecedingTable
     */
    public HashTable getWordPrecedingTable() {
        return this.wordPrecedingTable;
    }

    /**
     * Getter method for wordFollowingList
     * @return the wordFollowingList
     */
    public ArrayList<HashEntry> getWordFollowingList() {
        return wordFollowingList;
    }
    
    /**
     * Getter method for wordPrecedingList
     * @return the wordPrecedingList
     */
    public ArrayList<HashEntry> getWordPrecedingList() {
        return wordPrecedingList;
    }

    /**
     * Processes the wordFollowingTable, wordPrecedingTable, wordFollowingList, and wordPrecedingList
     * @param wordList the list of all words to be processed
     */
    protected void process(ArrayList<String> wordList) {
        /* add all words to the tables */
        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).equals(getKey())) {
                if (i + 1 < wordList.size()) {
                    wordFollowingTable.update(wordList.get(i + 1), (wordFollowingTable.get(wordList.get(i + 1)) == -1 ?
                                                               1 : wordFollowingTable.get(wordList.get(i + 1)) + 1));
                }
                if (i - 1 > -1) {
                    wordPrecedingTable.update(wordList.get(i - 1), (wordPrecedingTable.get(wordList.get(i - 1)) == -1 ?
                                                               1 : wordPrecedingTable.get(wordList.get(i - 1)) + 1));
                }
            }
        }

        /* create the lists of entries and sort them */
        this.wordFollowingList = this.wordFollowingTable.exportArray();
        this.wordPrecedingList = this.wordPrecedingTable.exportArray();
        this.wordFollowingList.sort(new HashEntry.ValueCompare());
        this.wordPrecedingList.sort(new HashEntry.ValueCompare());

        /* add rankings to the lists based on their ordering */
        for (int i = 0; i < wordFollowingList.size(); i++) {
            wordFollowingTable.updateRank(wordFollowingList.get(i).getKey(), wordFollowingList.size() - i);
        }
        for (int i = 0; i < wordPrecedingList.size(); i++) {
            wordPrecedingTable.updateRank(wordPrecedingList.get(i).getKey(), wordPrecedingList.size() - i);
        }
    }
}
