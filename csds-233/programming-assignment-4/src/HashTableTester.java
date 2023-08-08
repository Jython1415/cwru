import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Field;

public class HashTableTester {
    
    public static HashEntry[] getTable(HashTable table) throws Exception{
        Field array = table.getClass().getDeclaredField("table");
        array.setAccessible(true);
        return (HashEntry[])array.get(table);
    }

    public static int hashCode(String key, int tableSize) {
        return Math.abs(key.hashCode()) % tableSize;
    }

    /**
     * Unit tests for the constructors and getter/setter methods
     */
    @Test
    public void testHashTable() {
        HashTable t1 = new HashTable();

        Assert.assertEquals(t1.getSize(), 0);
        Assert.assertEquals(t1.getCapacity(), 1009);

        HashTable t2 = new HashTable(1);
        
        Assert.assertEquals(t2.getSize(), 0);
        Assert.assertEquals(t2.getCapacity(), 1);
    }

    /**
     * Unit tests for the put method (both versions)
     */
    @Test
    public void testPut() throws Exception {
        // test initial insertion
        HashTable t1 = new HashTable();
        t1.put("a", 1);
        Assert.assertEquals(1, getTable(t1)[hashCode("a", getTable(t1).length)].getValue());
        t1.put("b", 2);
        Assert.assertEquals(2, getTable(t1)[hashCode("b", getTable(t1).length)].getValue());

        // test duplicate keys
        HashTable t2 = new HashTable();
        t2.put("a", 1);
        Assert.assertEquals(1, getTable(t2)[hashCode("a", getTable(t2).length)].getValue());
        t2.put("a", 2);
        Assert.assertEquals(2, getTable(t2)[(hashCode("a", getTable(t2).length) + 1) % getTable(t2).length].getValue());
        t2.put("a", 3);
        Assert.assertEquals(3, getTable(t2)[(hashCode("a", getTable(t2).length) + 5) % getTable(t2).length].getValue());

        // test collisions with different keys
        HashTable t3 = new HashTable(10);
        t3.put("a", 1);
        t3.put("k", 2);
        Assert.assertEquals(1, getTable(t3)[(hashCode("a", getTable(t3).length)) % getTable(t3).length].getValue());
        Assert.assertEquals(2, getTable(t3)[(hashCode("a", getTable(t3).length) + 1) % getTable(t3).length].getValue());

        // test rehashing
        HashTable t4 = new HashTable(5);
        t4.put("a", 1);
        t4.put("b", 2);
        t4.put("c", 3);
        Assert.assertEquals(5, t4.getCapacity());
        t4.put("d", 4);
        Assert.assertEquals(11, t4.getCapacity());
        t4.put("e", 5);
        Assert.assertEquals(11, t4.getCapacity());
        t4.put("f", 6);
        Assert.assertEquals(11, t4.getCapacity());
        t4.put("g", 7);
        Assert.assertEquals(11, t4.getCapacity());
        t4.put("h", 8);
        Assert.assertEquals(23, t4.getCapacity());
        Assert.assertEquals(1, t4.get("a"));
        Assert.assertEquals(2, t4.get("b"));
        Assert.assertEquals(3, t4.get("c"));
        Assert.assertEquals(4, t4.get("d"));
        Assert.assertEquals(5, t4.get("e"));
        Assert.assertEquals(6, t4.get("f"));
        Assert.assertEquals(7, t4.get("g"));
        Assert.assertEquals(8, t4.get("h"));
    }

    /**
     * Unit tets for the update method
     */
    @Test
    public void testUpdate() {
        // test when the key is not in the table
        HashTable t1 = new HashTable();
        t1.update("a", 1);
        Assert.assertEquals(1, t1.get("a"));

        // test when the key is found in one search
        HashTable t2 = new HashTable();
        t2.put("a", 1);
        Assert.assertEquals(1, t2.get("a"));
        t2.update("a", 2);
        Assert.assertEquals(2, t2.get("a"));

        // test when there is collision
        HashTable t3 = new HashTable(10);
        t3.put("a", 1);
        t3.put("k", 2);
        Assert.assertEquals(2, t3.get("k"));
        t3.update("k", 3);
        Assert.assertEquals(3, t3.get("k"));
    }

    /**
     * Unit tests for the updateRank method
     */
    @Test
    public void testUpdateRank() {
        // test when key is not in the table
        HashTable t1 = new HashTable();
        Assert.assertEquals(-1, t1.updateRank("a", 1));
        Assert.assertEquals(-1, t1.get("a"));

        // test when the key can be found
        HashTable t2 = new HashTable();
        t2.put("a", 1);
        t2.updateRank("a", 2);
        Assert.assertEquals(1, t2.get("a"));
        Assert.assertEquals(2, t2.getRank("a"));
    }

    /**
     * Unit tests for the getRank method
     */
    @Test
    public void testGetRank() {
        // test when the key can be found
        HashTable t1 = new HashTable();
        t1.put("a", 1);
        t1.updateRank("a", 2);
        Assert.assertEquals(1, t1.get("a"));
        Assert.assertEquals(2, t1.getRank("a"));

        // test when the key is not in the table
        HashTable t2 = new HashTable();
        Assert.assertEquals(-1, t2.getRank("a"));
        Assert.assertEquals(-1, t2.get("a"));
    }

    /**
     * Unit tests for the get method (both versions)
     */
    @Test
    public void testGet() {
        // test when the key can be found
        HashTable t1 = new HashTable();
        t1.put("a", 1);
        Assert.assertEquals(1, t1.get("a"));

        // test when the key cannot be found
        HashTable t2 = new HashTable(10);
        t2.put("k", 1);
        Assert.assertEquals(-1, t2.get("a"));
    }

    /**
     * Unit tests for the exportArray method
     */
    @Test
    public void testExportArray() {
        // test with an empty table
        HashTable t1 = new HashTable();
        Assert.assertEquals(0, t1.exportArray().size());

        // test with a table with one entry
        HashTable t2 = new HashTable();
        t2.put("a", 1);
        Assert.assertEquals(1, t2.exportArray().size());
        Assert.assertEquals(1, t2.exportArray().get(0).getValue());

        // test with a table with many keys
        HashTable t3 = new HashTable();
        t3.put("a", 1);
        t3.put("b", 2);
        t3.put("c", 3);
        t3.put("d", 4);
        t3.put("e", 5);
        t3.put("f", 6);
        t3.put("g", 7);
        t3.put("h", 8);
        Assert.assertEquals(8, t3.exportArray().size());
    }

    /**
     * Unit tests for the nextPrime method
     */
    @Test
    public void testNextPrime() {
        // test for low primes
        Assert.assertEquals(2, HashTable.nextPrime(1));
        Assert.assertEquals(3, HashTable.nextPrime(2));
        Assert.assertEquals(101, HashTable.nextPrime(99));

        // test for mid-range primes (1,000-50,000)
        Assert.assertEquals(1217, HashTable.nextPrime(1215));
        Assert.assertEquals(4373, HashTable.nextPrime(4370));
        Assert.assertEquals(10789, HashTable.nextPrime(10785));

        // test for large primes (1,000,000-10,000,000)
        Assert.assertEquals(1000859, HashTable.nextPrime(1000850));
        Assert.assertEquals(5000623, HashTable.nextPrime(5000620));
        Assert.assertEquals(9001481, HashTable.nextPrime(9001470));
    }
}
