package stats.utils;

import stats.domain.Statistics;

public class StatisticsAccumulator {
    private long count;
    private double sum;
    private double min;
    private double max;

    public StatisticsAccumulator() {
        reset();
    }

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
        this.max = Math.max(this.max, other.max);
    }

    public void reset() {
        this.count = 0;
        this.sum = 0;
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
    }

    public Statistics toStats() {
        return new Statistics(sum, max, min, count);
    }
}
