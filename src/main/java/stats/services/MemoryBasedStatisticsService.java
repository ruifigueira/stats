package stats.services;

import java.time.Clock;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import stats.domain.Statistics;
import stats.domain.Transaction;
import stats.utils.StatisticsAccumulator;

import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;

@Service
public class MemoryBasedStatisticsService implements StatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryBasedStatisticsService.class);

    static class Key implements Comparable<Key> {

        private static final AtomicLong ID_GENERATOR = new AtomicLong();

        long id;
        long timestamp;

        Key(long timestamp) {
            this.id = ID_GENERATOR.getAndIncrement();
            this.timestamp = timestamp;
        }

        public long getTimestamp() {
            return timestamp;
        }

        @Override
        public int compareTo(Key o) {
            int compare = Longs.compare(this.timestamp, timestamp);
            if (compare != 0) return compare;
            return Longs.compare(this.id, o.id);
        }

        public static Key create(Transaction transaction) {
            return new Key(transaction.getTimestamp());
        }
    }

    private static final int TIME_FRAME_DURATION = 60000;
    private final Clock clock;
    // no need to use the synchronized version because we'll wrap iteration and insertion
    // calls in a synchronized block
    private SortedMap<Key, Transaction> transactions = Maps.newTreeMap();

    public MemoryBasedStatisticsService(Clock clock) {
        this.clock = clock;
    }

    @Override
    public boolean register(Transaction transaction) {
        long end = clock.millis();
        long start = end - TIME_FRAME_DURATION;
        if (transaction.getTimestamp() > end || transaction.getTimestamp() <= start) {
            LOGGER.warn("Transaction timestamp {}  not in the range {} to {}",
                    transaction.getTimestamp(),
                    start,
                    end);
            return false;
        }

        synchronized (transactions) {
            removeTransactionsOutOfFrame();
            transactions.put(Key.create(transaction), transaction);
        }

        return true;
    }

    @Override
    public Statistics getStatistics() {
        StatisticsAccumulator accumulator = new StatisticsAccumulator();

        synchronized (transactions) {
            removeTransactionsOutOfFrame();

            for (Transaction transaction : transactions.values()) {
                accumulator.accumulate(transaction.getAmount());
            }
        }
        return accumulator.toStats();
    }

    protected void removeTransactionsOutOfFrame() {
        // remove transactions out of time frame
        long start = clock.millis() - TIME_FRAME_DURATION;

        for (Iterator<Entry<Key, Transaction>> iterator = transactions.entrySet().iterator(); iterator.hasNext();) {
            Entry<Key, Transaction> entry = iterator.next();
            if (entry.getKey().getTimestamp() <= start) {
                LOGGER.warn("Transaction timestamp is now older than 60 sec.");
                iterator.remove();
            } else {
                // because transactions are sorted, we can be sure that
                // no more transactions need to be removed
                break;
            }
        }
    }
}
