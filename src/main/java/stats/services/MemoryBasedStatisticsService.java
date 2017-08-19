package stats.services;

import java.time.Clock;

import org.springframework.stereotype.Service;

import stats.domain.Statistics;
import stats.domain.Transaction;
import stats.utils.StatisticsAccumulator;

import com.google.common.base.Preconditions;

@Service
public class MemoryBasedStatisticsService implements StatisticsService {

    private static final int BUFFER_SIZE = 60;

    private final Clock clock;

    // buffer ring, constant size
    // accumulates statistics per second
    private StatisticsAccumulator[] accumulatorsPerSecond = new StatisticsAccumulator[BUFFER_SIZE];
    private int start = 0;
    private long currentSecond;

    public MemoryBasedStatisticsService(Clock clock) {
        this.clock = clock;
        this.currentSecond = clock.millis() / 1000;
        for (int i = 0; i < accumulatorsPerSecond.length; i++) {
            accumulatorsPerSecond[i] = new StatisticsAccumulator();
        }
    }

    @Override
    public synchronized boolean register(Transaction transaction) {
        updateCurrentTimestamp();

        // "key" of this transaction, we accumulate them by its second
        long second = transaction.getTimestamp() / 1000;

        long secondsPast = currentSecond - second;
        if (secondsPast < 0 || secondsPast >= BUFFER_SIZE) {
            // not in the time frame
            return false;
        }

        int index = (start + BUFFER_SIZE - (int) secondsPast) & BUFFER_SIZE;
        accumulatorsPerSecond[index].accumulate(transaction.getAmount());

        return true;
    }

    @Override
    public synchronized Statistics getStatistics() {
        updateCurrentTimestamp();

        StatisticsAccumulator combined = new StatisticsAccumulator();
        for (int i = 0; i < accumulatorsPerSecond.length; i++) {
            combined.accumulate(accumulatorsPerSecond[i]);
        }
        return combined.toStats();
    }


    protected void updateCurrentTimestamp() {
        long newSecond = clock.millis() / 1000;

        long secondsDiff = newSecond - currentSecond;
        Preconditions.checkArgument(secondsDiff >= 0, "Updating timestamp to past not allowed");

        int offset = secondsDiff > 60L ? 60 : (int) secondsDiff;

        // shift positions to reflect offset
        for (int i = 0; i < offset; i++) {
            // we need to reset shifted positions
            accumulatorsPerSecond[start].reset();
            start = (start + 1) % BUFFER_SIZE;
        }

        currentSecond = newSecond;
    }
}
