package stats.services;

import org.springframework.stereotype.Service;

import stats.domain.Statistics;
import stats.domain.Transaction;

@Service
public class MemoryBasedStatisticsService implements StatisticsService {

    @Override
    public void register(Transaction transaction) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Statistics getStatistics() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
