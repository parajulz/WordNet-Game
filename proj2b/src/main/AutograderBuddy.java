package main;

import browser.NgordnetQueryHandler;
import browser.NgordnetServer;
import ngrams.NGramMap;

public class AutograderBuddy {
    /** Paths for sample synset and hyponym files */
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";
    static NgordnetServer hns = new NgordnetServer();

    /** Returns a HyponymsHandler with the specified files */
    public static NgordnetQueryHandler getHyponymsHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {

        NGramMap nGramMap = new NGramMap(wordFile, countFile);

        HyponymsHandler hyponymsHandler = new HyponymsHandler(synsetFile, hyponymFile, nGramMap);
        hns.register("hyponyms", hyponymsHandler);
        return hyponymsHandler;
    }
}
