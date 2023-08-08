import java.util.ArrayList;

public class HashTable {
    /* Stores the number of entries stored in the table */
    private int size;

    /* Stores the load factor of the table */
    private double loadFactor;

    /* Stores the HashEntries */
    private HashEntry[] table;

    /* The load factor at which to expand and rehash */
    private static final double loadFactorLimit = 0.7;

    /* The default size for a table */
    private static final int defaultTableSize = 1009;

    public HashEntry[] getTable() {
        return this.table;
    }

    /**
     * Getter method for the size of the HashTable
     * @return the size (number of entries)
     */
    public int getSize() {
        return this.size;
    }

    protected void incrementSize() {
        this.size++;
    }

    /**
     * Provides the capacity of the table. The size of the internal array
     * @return the capacity
     */
    public int getCapacity() {
        return getTable().length;
    }

    /**
     * Creates a new HashTable with a size of 1009
     */
    public HashTable() {
        table = new HashEntry[HashTable.defaultTableSize];

        this.size = 0;
        this.loadFactor = 0.0;
    }

    /**
     * Creates a new HashTable at the specified size
     * @param tableSize the size of the table
     */
    public HashTable(int tableSize) {
        table = new HashEntry[tableSize];
        
        this.size = 0;
        this.loadFactor = 0.0;
    }

    /**
     * Stores the key-value pair in the HashTable
     * @param key the key to insert
     * @param value the value associated with the key
     */
    public void put(String key, int value) {
        incrementSize();
        updateLoadFactor();
        rehashIfNeeded();

        putEntry(new HashEntry(key, value), getTable());
    }

    public void put(String key, int value, int hashCode) {
        incrementSize();
        updateLoadFactor();
        rehashIfNeeded();

        putEntry(new HashEntry(key, value), getTable(), hashCode);
    }

    /**
     * Updates the value associated with the key. If it doesn't exist, then it is added to the table
     * @param key the key to search for
     * @param value the new value
     */
    public void update(String key, int value) {
        int currentIndex = findIndex(key, getTable());
        
        if (currentIndex == -1) {
            put(key, value);
        }
        else {
            this.table[currentIndex].setValue(value);
        }
    }

    public int updateRank(String key, int rank) {
        int currentIndex = findIndex(key, getTable());

        if (currentIndex == -1) {
            return -1;
        }
        else {
            this.table[currentIndex].setRank(rank);
            return currentIndex;
        }
    }

    public int getRank(String key) {
        int currentIndex = findIndex(key, getTable());

        if (currentIndex == -1) {
            return -1;
        }
        else {
            return this.table[currentIndex].getRank();
        }
    }

    /**
     * Returns the value associated with the specified key. If there is no entry, then returns -1
     * @param key the key to look for
     * @return -1 or the value
     */
    public int get(String key) {
        int currentIndex = findIndex(key, getTable());

        if (currentIndex == -1) {
            return -1;
        }

        return this.table[currentIndex].getValue();
    }

    /**
     * Method for testing purposes
     * @param key the key to look for
     * @param hashCode the hash of the key
     * @return -1 or the value
     */
    public int get(String key, int hashCode) {
        int currentIndex = findIndex(key, getTable(), hashCode);

        if (currentIndex == -1) {
            return -1;
        }

        return this.table[currentIndex].getValue();
    }

    public ArrayList<HashEntry> exportArray() {
        ArrayList<HashEntry> entries = new ArrayList<HashEntry>();
        for (HashEntry entry : this.table) {
            if (entry != null) {
                entries.add(entry);
            }
        }

        return entries;
    }

    /**
     * Expands the table size and rehashes all entries to the new table
     */
    protected void rehash() {
        HashEntry[] newTable = new HashEntry[HashTable.nextPrime(getCapacity() * 2)];

        for (HashEntry entry : getTable()) {
            if (entry != null) {
                putEntry(entry, newTable);
            }
        }

        this.table = newTable;
    }

    /**
     * Adds a HashEntry to a table
     * @param entry the entry to add
     * @param table the table to add to
     * @return -1 if unsuccessful, otherwise the index the entry was inserted at
     */
    protected int putEntry(HashEntry entry, HashEntry[] table) {
        int currentIndex = findOpenIndex(entry.getKey(), table);
        
        if (currentIndex == -1) {
            return -1;
        }

        table[currentIndex] = entry;
        return currentIndex;
    }

    /**
     * putEntry, but for testing
     * @param entry the entry to add
     * @param table the table to add to
     * @param hashCode the custom hash code
     * @return -1 if unsuccessful, otherwise the index the entry was inserted at
     */
    protected int putEntry(HashEntry entry, HashEntry[] table, int hashCode) {
        int currentIndex = findOpenIndex(entry.getKey(), table, hashCode);
        
        if (currentIndex == -1) {
            return -1;
        }

        table[currentIndex] = entry;
        return currentIndex;
    }

    /**
     * Finds the index of a given key. -1 if it cannot be found
     * @param key the key to look for
     * @return the index of the key
     */
    protected int findIndex(String key, HashEntry[] table) {
        int currentIndex = Math.abs(key.hashCode()) % table.length;
        int counter = 1;

        while (table[currentIndex] != null && (!table[currentIndex].getKey().equals(key) && counter < table.length)) {
            currentIndex = (currentIndex + (counter*counter++)) % table.length;
        }

        if (counter >= table.length || table[currentIndex] == null) {
            return -1;
        }

        return currentIndex;
    }

    /**
     * findIndex, but for testing
     * @param key the key to search for
     * @param hashCode the custom hash
     * @return the index of the key
     */
    protected int findIndex(String key, HashEntry[] table, int hashCode) {
        int currentIndex = Math.abs(hashCode) % table.length;
        int counter = 1;

        while (table[currentIndex] != null && (!table[currentIndex].getKey().equals(key) && counter < table.length)) {
            currentIndex = (currentIndex + (counter*counter++)) % table.length;
        }

        if (counter >= table.length || table[currentIndex] == null) {
            return -1;
        }

        return currentIndex;
    }

    /**
     * Finds the index where the key can be inserted. -1 if it cannot be found
     * @param key the key to insert
     * @return the index
     */
    protected int findOpenIndex(String key, HashEntry[] table) {
        int currentIndex = Math.abs(key.hashCode()) % table.length;
        int counter = 1;

        while (table[currentIndex] != null && counter < table.length + 1) {
            currentIndex = (currentIndex + (counter*counter++)) % table.length;
        }

        if (counter >= table.length) {
            return -1;
        }

        return currentIndex;
    }

    /**
     * findOpenIndex, but for testing
     * @param key the key to insert
     * @param hashCode the custom hash code
     * @return the index
     */
    protected int findOpenIndex(String key, HashEntry[] table, int hashCode) {
        int currentIndex = Math.abs(hashCode) % table.length;
        int counter = 1;

        while (table[currentIndex] != null && counter < table.length) {
            currentIndex = (currentIndex + (counter*counter++)) % table.length;
        }

        if (counter >= table.length) {
            return -1;
        }

        return currentIndex;
    }

    /**
     * Updates the load factor field
     */
    protected void updateLoadFactor() {
        this.loadFactor = this.size / (double)getCapacity();
    }

    /**
     * rehashes the table if needed
     */
    protected void rehashIfNeeded() {
        if (this.loadFactor > HashTable.loadFactorLimit) {
            rehash();
        }
    }

    /**
     * Method for finding the next prime
     * @param input the lower limit
     * @return the next prime after the lower limit
     * @author not me
     */
    protected static int nextPrime(int input){
        input++;
        int limit = (int) Math.sqrt(input);
        int counter;
        while(true) {
            counter = 0;
            for(int i = 2; i <= limit && counter == 0; i ++) {
                if (input % i == 0)
                    counter++;
            }

            if (counter == 0) {
                return input;
            }
            else {
                input++;
            }
        }
    }
}
