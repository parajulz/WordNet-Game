package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import helperclasses.ParserFiles;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private final ParserFiles wordNet;
    private final NGramMap nGramMap;

    public HyponymsHandler(String synsetFile, String hyponymsFile, NGramMap nGramMap) {
        this.wordNet = new ParserFiles(synsetFile, hyponymsFile);
        this.nGramMap = nGramMap;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> queryWords = q.words();
        int k = q.k();

        // get hyponym for each word in query (multiple or singular)
        List<Set<String>> hyponymsList = new ArrayList<>();
        for (String word : queryWords) {
            hyponymsList.add(new HashSet<>(wordNet.findHyponyms(word)));
        }

        // find same hyponyms for multiple query words
        Set<String> samehyps = new HashSet<>(hyponymsList.get(0));
        for (int i = 1; i < hyponymsList.size(); i++) {
            samehyps.retainAll(hyponymsList.get(i));
        }

        // store frequency sum for each hyponym in hashmap
        Map<String, Double> hyponymMap = new HashMap<>();
        for (String hyponym : samehyps) {
            TimeSeries ts = nGramMap.countHistory(hyponym, q.startYear(), q.endYear());
            double sum = 0.0;
            for (Integer year : ts.keySet()) {
                if (year >= q.startYear() && year <= q.endYear()) {
                    sum += ts.get(year);
                }
            }
            // add to the map if frequency is greater than 0, or if k == 0 (add all regardless of freq)
            if (sum > 0 || (k == 0 && sum == 0)) {
                hyponymMap.put(hyponym, sum);
            }
        }

        // priority queue to get most popular hyponyms
        PriorityQueue<String> pq = new PriorityQueue<>(
            new Comparator<String>() { // comparator syntax: www.geeksforgeeks.org/priority-queue-class-in-java/#
                @Override
                public int compare(String a, String b) {
                    int hyponymCompare = Double.compare(hyponymMap.get(b), hyponymMap.get(a));
                    if (hyponymCompare == 0) {
                        return a.compareTo(b);
                    }
                    return hyponymCompare;
                }
            });
        pq.addAll(hyponymMap.keySet());

        // pop max from priority max heap and add to new array list
        List<String> popularHyponyms = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && (k == 0 || count < k)) {
            popularHyponyms.add(pq.poll());
            count++;
        }

        // Sort the final list lexicographically
        Collections.sort(popularHyponyms);

        // return the sorted hyponyms list or empty string list
        if (popularHyponyms.isEmpty()) {
            return "[]";
        } else {
            return popularHyponyms.toString();
        }
    }
}
