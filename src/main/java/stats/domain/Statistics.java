package stats.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Statistics {

    /**
     * sum is a double specifying the total sum of transaction value
     * in the last 60 seconds
     */
    private final double sum;

    /**
     * avg is a double specifying the average amount of transaction
     * value in the last 60 seconds
     */
    private final double avg;

    /**
     * max is a double specifying single highest transaction value in
     * the last 60 seconds
     */
    private final double max;

    /**
     * min is a double specifying single lowest transaction value in
     * the last 60 seconds
     */
    private final double min;

    /**
     * count is a long specifying the total number of transactions
     * happened in the last 60 seconds
     */
    private final long count;

    @JsonCreator
    public Statistics(
            @JsonProperty("sum") double sum,
            @JsonProperty("max") double max,
            @JsonProperty("min") double min,
            @JsonProperty("count") long count) {
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.count = count;
        this.avg = this.count > 0L ? this.sum / this.count : Double.NaN;
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return avg;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getCount() {
        return count;
    }
}
