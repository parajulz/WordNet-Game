package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import org.knowm.xchart.XYChart;
import plotting.Plotter;

import java.util.ArrayList;
import java.util.List;

public class HistoryHandler extends NgordnetQueryHandler {

    private NGramMap map;

    public HistoryHandler(NGramMap map) {
        this.map = map;
    }
    @Override
    public String handle(NgordnetQuery q) {

        List<String> words = q.words();

        int startYear = q.startYear();
        int endYear = q.endYear();

        ArrayList<TimeSeries> timeSeriesList = new ArrayList<>();
        ArrayList<String> graphLabel = new ArrayList<>();

        for (String word : words) {
            TimeSeries timeSeries = map.weightHistory(word, startYear, endYear);

            if (timeSeries.isEmpty()) {
                return null;
            } else {
                timeSeriesList.add(timeSeries);
                graphLabel.add(word);
            }
        }

        XYChart chart = Plotter.generateTimeSeriesChart(graphLabel, timeSeriesList);
        String encodedImage = Plotter.encodeChartAsString(chart);

        return encodedImage;
    }
}
