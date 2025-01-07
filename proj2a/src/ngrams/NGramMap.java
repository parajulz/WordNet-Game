package ngrams;
import edu.princeton.cs.algs4.In;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    Map<String, TimeSeries> words; // Years and number of times it occurred
    Map<Integer, Double> count; // Year to number of words
    TimeSeries temp;

    public NGramMap(String wordsFilename, String countsFilename) {

        words = new TreeMap<>();
        count = new TreeMap<>();
        temp = new TimeSeries();

        wordsFileReader(wordsFilename);
        countFileReader(countsFilename);
    }

    // https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/scatter-chart.htm#CIHDEACI
    private void wordsFileReader(String fileName) {
        In in = new In(fileName);

        while (in.hasNextLine()) {
            String nextLine = in.readLine();

            String[] splitLine = nextLine.split("\t");

            String word = splitLine[0];

            int year = Integer.parseInt(splitLine[1]);
            double value = Double.parseDouble(splitLine[2]);

            if (!words.containsKey(word)) {
                words.put(word, new TimeSeries());
            }

            words.get(word).put(year, value);
        }
    }

    // https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/scatter-chart.htm#CIHDEACI
    private void countFileReader(String fileName) {
        In in = new In(fileName);
        while (in.hasNextLine()) {
            String nextLine = in.readLine();

            String[] splitLine = nextLine.split(",");

            int year = Integer.parseInt(splitLine[0]);
            double value = Double.parseDouble(splitLine[1]);

            count.put(year, value);
            temp.put(year, value);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {

        if (!words.containsKey(word)) {
            return new TimeSeries();
        }

        TimeSeries history = words.get(word);

        TimeSeries copy = new TimeSeries(history, startYear, endYear);

        return copy;
    }

    /**
     * * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
    * */
    public TimeSeries countHistory(String word) {

        if (!words.containsKey(word)) {
            return new TimeSeries();
        }

        TimeSeries wordHistory = words.get(word);

        TimeSeries copy = new TimeSeries(wordHistory, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);

        return copy;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
    */
    public TimeSeries totalCountHistory() {
        TimeSeries copy = new TimeSeries(temp, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
        return copy;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {

        if (!words.containsKey(word)) {
            return new TimeSeries();
        }

        TimeSeries timeseries = new TimeSeries(words.get(word), startYear, endYear);
        TimeSeries total = new TimeSeries(totalCountHistory(), startYear, endYear);

        return timeseries.dividedBy(total);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {

        if (!words.containsKey(word)) {
            return new TimeSeries();
        }

        TimeSeries timeseries = countHistory(word);
        TimeSeries totalCount = totalCountHistory();

        return timeseries.dividedBy(totalCount);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
    */

    public TimeSeries summedWeightHistory(Collection<String> wurds, int startYear, int endYear) {

        TimeSeries result = new TimeSeries();

        for (String word : wurds) {
            TimeSeries timeseries = countHistory(word, startYear, endYear);

            TimeSeries total = new TimeSeries(totalCountHistory(), startYear, endYear);

            TimeSeries wordHistory = timeseries.dividedBy(total);
            result = result.plus(wordHistory);
        }

        return result;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> wurds) {

        TimeSeries result = new TimeSeries();

        for (String word : wurds) {
            TimeSeries wordTimeseries = countHistory(word);
            TimeSeries totalCount = totalCountHistory();

            TimeSeries summedHistory = wordTimeseries.dividedBy(totalCount);

            result = result.plus(summedHistory);
        }
        return result;
    }
}
