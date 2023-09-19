import org.junit.Assert;
import org.junit.Test;

public class WordStatTester {
    private static String[] testWords1 = new String[]{"a", "a", "b", "c", "D", "a", "b"}; 
    private static String[] testWords2 = new String[]{"a", "a", "a", "b", "b", "c"};
    private static String[] testWords3 = new String[]{"a", "a", "a", "a", "b", "b", "b"};
    private static String[] testWords4 = new String[]{"a", "b", "b", "a", "b"};

    @Test
    public void testWordCount() {
        // count is 0
        WordStat s1 = new WordStat(testWords1);
        Assert.assertEquals(0, s1.wordCount("e"));

        // count is 1
        Assert.assertEquals(1, s1.wordCount("C"));
        Assert.assertEquals(1, s1.wordCount("d"));

        // count is many
        Assert.assertEquals(3, s1.wordCount("a"));
    }

    @Test
    public void testWordPairCount() throws Exception {
        // count is 0
        WordStat s1 = new WordStat(testWords1);
        Assert.assertEquals(0, s1.wordPairCount("C", "b"));

        // count is 1
        Assert.assertEquals(1, s1.wordPairCount("B", "C"));
        Assert.assertEquals(1, s1.wordPairCount("a", "a"));

        // count is many
        Assert.assertEquals(2, s1.wordPairCount("a", "B"));
    }

    @Test
    public void testWordRank() {
        // word is not there
        WordStat s1 = new WordStat(testWords2);
        Assert.assertEquals(0, s1.wordRank("d"));

        // word is first
        Assert.assertEquals(1, s1.wordRank("a"));

        // word is last
        Assert.assertEquals(3, s1.wordRank("c"));

        // word is somewhere in between
        Assert.assertEquals(2, s1.wordRank("b"));
    }

    @Test
    public void testWordPairRank() {
        // pair is not there
        WordStat s1 = new WordStat(testWords3);
        Assert.assertEquals(0, s1.wordPairRank("b", "a"));

        // pair is first
        Assert.assertEquals(1, s1.wordPairRank("a", "a"));

        // pair is last
        Assert.assertEquals(3, s1.wordPairRank("a", "b"));

        // pair is somewhere in between
        Assert.assertEquals(2, s1.wordPairRank("b", "b"));
    }

    @Test
    public void testMostCommonWords() {
        // 1 word
        WordStat s1 = new WordStat(testWords1);
        Assert.assertArrayEquals(new String[]{"a"}, s1.mostCommonWords(1));

        // multiple words
        Assert.assertArrayEquals(new String[]{"a", "b"}, s1.mostCommonWords(2));

        // all words
        Assert.assertArrayEquals(new String[]{"a", "b", "d", "c"}, s1.mostCommonWords(4));
    }

    @Test
    public void testLeastCommonWords() {
        // 1 word
        WordStat s1 = new WordStat(testWords1);
        Assert.assertArrayEquals(new String[]{"c"}, s1.leastCommonWords(1));

        // multiple words
        Assert.assertArrayEquals(new String[]{"c", "d"}, s1.leastCommonWords(2));

        // all words
        Assert.assertArrayEquals(new String[]{"c", "d", "b", "a"}, s1.leastCommonWords(4));
    }

    @Test
    public void testMostCommonWordPairs() {
        // 1 pair
        WordStat s1 = new WordStat(testWords3);
        Assert.assertArrayEquals(new String[]{"a a"}, s1.mostCommonWordPairs(1));

        // 2 pairs
        Assert.assertArrayEquals(new String[]{"a a", "b b"}, s1.mostCommonWordPairs(2));

        // all pairs
        Assert.assertArrayEquals(new String[]{"a a", "b b", "a b"}, s1.mostCommonWordPairs(3));
    }

    @Test
    public void testMostCommonColloc() {
        // following
        // 1
        WordStat s1 = new WordStat(testWords4);
        Assert.assertArrayEquals(new String[]{"b"}, s1.mostCommonCollocs(1, "a", 1));

        // multiple
        Assert.assertArrayEquals(new String[]{"b"}, s1.mostCommonCollocs(5, "a", 1));
        Assert.assertArrayEquals(new String[]{"b", "a"}, s1.mostCommonCollocs(2, "b", 1));

        // preceding
        // 1
        Assert.assertArrayEquals(new String[]{"b"}, s1.mostCommonCollocs(1, "a", -1));
        Assert.assertArrayEquals(new String[]{"a"}, s1.mostCommonCollocs(1, "b", -1));

        // multiple
        Assert.assertArrayEquals(new String[]{"a", "b"}, s1.mostCommonCollocs(2, "b", -1));
        Assert.assertArrayEquals(new String[]{"b"}, s1.mostCommonCollocs(3, "a", -1));
    }
}
