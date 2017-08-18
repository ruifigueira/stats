package stats.services;

import stats.domain.Statistics;
import stats.domain.Transaction;

public interface StatisticsService {

    public void register(Transaction transaction);

    public Statistics getStatistics();
}
