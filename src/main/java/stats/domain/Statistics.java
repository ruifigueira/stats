package stats.domain;

import java.util.Objects;

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

    public long getCount() {
        return count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum, max, min, count);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Statistics other = (Statistics) obj;
        return this.sum == other.sum &&
                this.max == other.max &&
                this.min == other.min &&
                this.count == other.count;
    }


}
