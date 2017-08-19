package stats.services;

import java.time.Clock;
import java.util.Iterator;
import java.util.SortedSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import stats.domain.Statistics;
import stats.domain.Transaction;

import com.google.common.collect.Sets;

@Service
public class MemoryBasedStatisticsService implements StatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryBasedStatisticsService.class);

    private static final int TIME_FRAME_DURATION = 60000;
    private final Clock clock;
    private SortedSet<Transaction> transactions = Sets.newTreeSet();

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
        transactions.add(transaction);
        return true;
    }

    @Override
    public Statistics getStatistics() {
        removeTransactionsOutOfFrame();

        double sum = 0d;
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        long count = transactions.size();

        for (Transaction transaction : transactions) {
            sum += transaction.getAmount();
            max = Math.max(max, transaction.getAmount());
            min = Math.min(min, transaction.getAmount());
        }

        return new Statistics(sum, max, min, count);
    }

    protected void removeTransactionsOutOfFrame() {
        // remove transactions out of time frame
        long start = clock.millis() - TIME_FRAME_DURATION;

        for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
            Transaction transaction = iterator.next();
            if (transaction.getTimestamp() <= start) {
                LOGGER.warn("Transaction timestamp is now older than 60 sec.");
                iterator.remove();
            }
        }
    }
}
