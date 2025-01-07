package main;
import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {

    private NGramMap map;

    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();

        int startYear = q.startYear();
        int endYear = q.endYear();

        //suggested by intelJ
        StringBuilder answer = new StringBuilder();

        for (String word: words) {
            TimeSeries wordHistory = map.weightHistory(word, startYear, endYear);

            answer.append(word).append(": {");

            for (int year: wordHistory.keySet()) {
                // suggested by intelJ
                answer.append(year).append("=").append(wordHistory.get(year)).append(", ");
            }

            if (answer.length() > 0) {
                answer.setLength(answer.length() - 2);
            }

            answer.append("}, ");

            if (answer.length() > 0) {
                answer.setLength(answer.length() - 2);
            }
            answer.append("\n");
        }
        return answer.toString();
    }
}
