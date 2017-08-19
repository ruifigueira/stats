package stats.utils;

import stats.domain.Statistics;

public class StatisticsAccumulator {
    private long count;
    private double sum;
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;

    public void accumulate(double val) {
        this.count++;
        this.sum += val;
        this.min = Math.min(this.min, val);
        this.max = Math.max(this.max, val);
    }

    public void accumulate(StatisticsAccumulator other) {
        this.count += other.count;
        this.sum += other.sum;
        this.min = Math.min(this.min, other.min);
        this.max = Math.min(this.max, other.max);
    }

    public Statistics toStats() {
        return new Statistics(sum, max, min, count);
    }
}
