import helperclasses.Graph;
import helperclasses.ParserFiles;

import java.util.Set;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FileReaderTests {
    private ParserFiles wordnet;

    public FileReaderTests() {
        wordnet = new ParserFiles("data/wordnet/synsets16.txt","data/wordnet/hyponyms16.txt");
    }

    @Test
    public void SynsetReaderTest () {
        Set<Integer> synsetIds = wordnet.getSynset().get("event");
        assertNotNull(synsetIds);
        assertTrue(synsetIds.contains(0));
    }

    @Test
    public void testFindHyponymsSingleWord() {
        ParserFiles parser = new ParserFiles("./data/wordnet/synsets11.txt","./data/wordnet/hyponyms11.txt");
        Set<String> hyponyms = parser.findHyponyms("descent");
        Set<String> expected = Set.of("descent", "jump", "parachuting");
        assertEquals(expected, hyponyms);
    }

    @Test
    public void testFindHyponymsMultipleWords() {
        ParserFiles parser = new ParserFiles("./data/wordnet/synsets16.txt","./data/wordnet/hyponyms16.txt");
        Set<String> hyponyms = parser.findHyponyms("change");
        Set<String> expected = Set.of("alteration", "change", "increase", "jump", "leap", "modification", "saltation", "transition");
        assertEquals(expected, hyponyms);
    }

    @Test
    public void testFindHyponymsEmptyWord() {
        ParserFiles parser = new ParserFiles("./data/wordnet/synsets11.txt","./data/wordnet/hyponyms11.txt");
        Set<String> hyponyms = parser.findHyponyms("nonexistent");
        Set<String> expected = Set.of();
        assertEquals(expected, hyponyms);
    }
}

