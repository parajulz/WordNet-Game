package helperclasses;

import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class ParserFiles {

    private HashMap<String, Set<Integer>> synset;
    private Graph graph;

    public ParserFiles(String synsetFile, String hyponymsFile) {
        synset = new HashMap<>();
        graph = new Graph();

        synsetReader(synsetFile);
        hyponymsReader(hyponymsFile);
    }

    // parses synsets file and adds nodes to the graph
    private void synsetReader(String synsetFile) {
        In in = new In(synsetFile);

        while (in.hasNextLine()) {
            String nextLine = in.readLine();
            String[] splitLine = nextLine.split(",");

            int synsetNum = Integer.parseInt(splitLine[0]);
            String[] words = splitLine[1].split(" ");

            graph.addNode(synsetNum);

            for (String word : words) {
                synset.putIfAbsent(word, new HashSet<>());
                synset.get(word).add(synsetNum);
                graph.addSynset(synsetNum, word);
            }
        }
    }

    // reads hyponyms and adds edges to the graph
    private void hyponymsReader(String hyponymsFile) {
        In in = new In(hyponymsFile);

        while (in.hasNextLine()) {
            String nextLine = in.readLine();
            String[] splitLine = nextLine.split(",");

            int synsetNum = Integer.parseInt(splitLine[0]);
            for (int i = 1; i < splitLine.length; i++) {
                int hyponymId = Integer.parseInt(splitLine[i]);
                graph.addEdge(synsetNum, hyponymId);
            }
        }
    }

    // map of words to synset IDs
    public HashMap<String, Set<Integer>> getSynset() {
        return synset;
    }

    // graph of synsets and hyponyms
    public Graph getGraph() {
        return graph;
    }

    // finds all hyponyms of the given word.
    // looks through synsets that are reachable from any synset containing the word
    public Set<String> findHyponyms(String word) {
        Set<String> hyponyms = new TreeSet<>();

        Set<Integer> synsetValues = synset.get(word);
        if (synsetValues != null && !synsetValues.isEmpty()) {
            for (Integer boi : synsetValues) {
                hyponyms.addAll(graph.hyponymDFS(boi));
            }
            hyponyms.add(word);
        }

        return hyponyms;
    }
}
