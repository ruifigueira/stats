package stats.services;

import stats.domain.Statistics;
import stats.domain.Transaction;

public interface StatisticsService {

    public boolean register(Transaction transaction);

    public Statistics getStatistics();
}
