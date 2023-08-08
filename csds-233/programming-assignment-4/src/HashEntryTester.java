import org.junit.Assert;
import org.junit.Test;
import java.util.Comparator;

public class HashEntryTester {
    
    /**
     * Unit tests for the constructor and all getters and setters
     */
    @Test
    public void testHashEntry() {
        HashEntry e1 = new HashEntry("a", 1);

        // tests for the constructor
        Assert.assertEquals("a", e1.getKey());
        Assert.assertEquals(1, e1.getValue());
        Assert.assertEquals(-1, e1.getRank());

        e1.setKey("b");
        e1.setValue(5);
        e1.setRank(10);

        // tests for the setters and getters
        Assert.assertEquals("b", e1.getKey());
        Assert.assertEquals(5, e1.getValue());
        Assert.assertEquals(10, e1.getRank());
    }

    /**
     * Unit tests for the compare method in ValueCompare
     */
    @Test
    public void testValueCompare() {
        Comparator<HashEntry> c = new HashEntry.ValueCompare();

        HashEntry e1 = new HashEntry("a", 5);
        HashEntry e2 = new HashEntry("b", 10);
        HashEntry e3 = new HashEntry("c", 10);

        Assert.assertTrue(c.compare(e1, e2) < 0);
        Assert.assertTrue(c.compare(e2, e1) > 0);
        Assert.assertTrue(c.compare(e2, e3) == 0);
        Assert.assertTrue(c.compare(e1, e1) == 0);
    }
}
