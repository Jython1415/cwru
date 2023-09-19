import org.junit.Assert;
import org.junit.Test;

public class TokenizerTester {
    
    /**
     * Unit tests for Tokenizer constructors
     * This also tests the wordList method
     */
    @Test
    public void testTokenizer() throws Exception{
        String message = "The input was not separated and normalized correctly";
        String badException = "The method should not have thrown an exception";
        
        String path = getClass().getResource("test.txt").getPath();

        // Check file input
        try {
            Assert.assertArrayEquals(message, new String[]{"this", "is", "an", "easy", "sentence", "this", "is", "slightly", "more", "difficult",
                                                        "thisis", "supposed", "to", "be", "even", "more", "difficult", "abcd", "efgh"},
                                              (new Tokenizer(path).wordList().toArray()));
        }
        catch (Exception e) {
            Assert.fail(badException + e.toString());
        }

        // Check with String[] input
        Assert.assertArrayEquals(message, new String[]{"a", "ab", "abc", "abcd"},
                                          (new Tokenizer(new String[]{"A---- ", "   AB-../", "abC___ --", "abcd"}).wordList().toArray()));
    }

    /**
     * Unit tests for normalizeWord
     */
    @Test
    public void testNormalizeWord() {
        String message = "The word was not normalized correctly";

        // check capitalization
        Assert.assertEquals(message, "a", Tokenizer.normalizeWord("A"));
        Assert.assertEquals(message, "a", Tokenizer.normalizeWord("a"));
        Assert.assertEquals(message, "abcdefghijklmnopqrstuvwxyz", Tokenizer.normalizeWord("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        Assert.assertEquals(message, "abcd", Tokenizer.normalizeWord("AbcD"));

        // check punctuation
        Assert.assertEquals(message, "", Tokenizer.normalizeWord("!#$%&'()*+,-./:;<=>?@[]^_`{|}~\""));
        Assert.assertEquals(message, "a", Tokenizer.normalizeWord(",a,"));
        Assert.assertEquals(message, "aa", Tokenizer.normalizeWord("a,a"));
        Assert.assertEquals(message, "", Tokenizer.normalizeWord("“"));

        // check spaces
        Assert.assertEquals(message, "", Tokenizer.normalizeWord(" "));
        Assert.assertEquals(message, "", Tokenizer.normalizeWord("     "));
        Assert.assertEquals(message, "a", Tokenizer.normalizeWord(" a "));
    }
}
