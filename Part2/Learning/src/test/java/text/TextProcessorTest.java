package text;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TextProcessorTest {
    
    public TextProcessorTest() {
        
    }

    @Test
    public void testIsCurrency() {
        assertTrue(TextProcessor.isCurrency("$499"));
        assertTrue(TextProcessor.isCurrency("$4"));

        assertFalse(TextProcessor.isCurrency("$"));
        assertFalse(TextProcessor.isCurrency("hello"));
        assertFalse(TextProcessor.isCurrency("423"));
    }

    @Test
    public void testIsSymbol() {
        assertTrue(TextProcessor.isSymbol("$"));
        assertTrue(TextProcessor.isSymbol("@"));
        assertTrue(TextProcessor.isSymbol("!"));
        assertTrue(TextProcessor.isSymbol("$@#@#@()!"));

        assertFalse(TextProcessor.isSymbol("hello"));
        assertFalse(TextProcessor.isSymbol("1234"));
        assertFalse(TextProcessor.isSymbol("@#$@#@H"));
        assertFalse(TextProcessor.isSymbol("Michael: "));
    }
    
    @Test
    public void testWeakStem() {
        assertEquals("book", TextProcessor.weakStem("books"));
        assertEquals("s", TextProcessor.weakStem("s"));
    }
    
    @Test
    public void testPorterStem() {
        assertEquals("book", TextProcessor.porterStem("books"));

        assertEquals("connect", TextProcessor.porterStem("connected"));
        assertEquals("connect", TextProcessor.porterStem("connection"));
        
        // Stemmer sometimes cuts off endings but they still conflate well
        assertEquals("relat", TextProcessor.porterStem("relational"));
        
        assertEquals("allow", TextProcessor.porterStem("allowance"));
        
        assertEquals("adjust", TextProcessor.porterStem("adjustable"));
    }
}