package helperclasses;

import java.util.*;

public class Graph {

    private Map<Integer, Set<Integer>> adjList;
    private Map<Integer, Set<String>> synsetSet;

    public Graph() {
        adjList = new HashMap<>();
        synsetSet = new HashMap<>();
    }

    public void addNode(int synset) {
        if (!adjList.containsKey(synset)) {
            adjList.put(synset, new HashSet<>());
        }
        if (!synsetSet.containsKey(synset)) {
            synsetSet.put(synset, new HashSet<>());
        }
    }

    public void addEdge(int synset, int hyponym) {
        addNode(synset);
        addNode(hyponym);
        adjList.get(synset).add(hyponym);
    }

    public Set<Integer> getHyponymValue(int synset) {
        if (adjList.containsKey(synset)) {
            return adjList.get(synset);
        } else {
            return new HashSet<>();
        }
    }

    public Set<String> getSynsetValue(int synset) {
        if (synsetSet.containsKey(synset)) {
            return synsetSet.get(synset);
        } else {
            return new HashSet<>();
        }
    }

    public void addSynset(int synset, String word) {
        addNode(synset);
        synsetSet.get(synset).add(word);
    }

    // conducts DFS: recurses through each hyponym before traversing down to children before backtracking
    public Set<String> hyponymDFS(int synset) {
        Set<String> result = new HashSet<>();
        if (!adjList.containsKey(synset) && !synsetSet.containsKey(synset)) {
            return result;
        }

        Set<Integer> visited = new HashSet<>();
        hyponymDFSHelper(synset, visited, result);
        return result;
    }

    private void hyponymDFSHelper(int synset, Set<Integer> visited, Set<String> result) {
        if (visited.contains(synset)) {
            return;
        }
        visited.add(synset);

        // add words associated with this synset
        result.addAll(getSynsetValue(synset));

        // recursively search direct hyponyms
        for (int hyponym : getHyponymValue(synset)) {
            hyponymDFSHelper(hyponym, visited, result);
        }
    }
}
