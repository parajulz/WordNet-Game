package ngrams;

import java.util.List;
import java.util.TreeMap;
import java.util.ArrayList;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here. */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (Integer year : ts.keySet()) {
            if (year >= startYear && year <= endYear) {
                this.put(year, ts.get(year));
            }
        }
    }

    /**
     *  Returns all years for this time series in ascending order.
     */
    public List<Integer> years() {
        return new ArrayList<>(this.keySet());
    }

    /**
     *  Returns all data for this time series. Must correspond to the
     *  order of years().
     */
    public List<Double> data() {
        ArrayList<Double> list = new ArrayList<>();
        for (Integer year : years()) {
            list.add(this.get(year));
        }
        return list;
    }
    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries timeseries = new TimeSeries();

        if (ts.years().isEmpty() && this.years().isEmpty()) {
            return timeseries;
        }

        for (Integer year : this.years()) {
            if (ts.containsKey(year)) {
                timeseries.put(year, this.get(year) + ts.get(year));

            } else {
                timeseries.put(year, this.get(year));
            }
        }

        for (Integer year : ts.years()) {
            if (!this.containsKey(year)) {
                timeseries.put(year, ts.get(year));
            }
        }

        return timeseries;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries timeseries = new TimeSeries();

        for (Integer year : this.years()) {
            if (ts.containsKey(year)) {
                if (ts.get(year) == 0) {
                    throw new IllegalArgumentException("Cannot divide by zero");
                }
                double value = this.get(year) / ts.get(year);
                timeseries.put(year, value);
            } else {
                throw new IllegalArgumentException(year + "Does not exist in current TimeSeries");
            }
        }
        return timeseries;
    }
}
