package stats.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

import stats.domain.Statistics;
import stats.domain.Transaction;
import stats.mocks.MockClock;

public class MemoryBasedStatisticsServiceTest {

    private MockClock clock;
    private MemoryBasedStatisticsService service;

    @Before
    public void setup() {
        clock = new MockClock(Instant.ofEpochSecond(60));
        service = new MemoryBasedStatisticsService(clock);
    }

    @Test
    public void testPostTransactions() {
        // transaction in the past, to ignore
        assertThat(service.register(new Transaction(0.1, 0))).isFalse();
        // regular transactions
        assertThat(service.register(new Transaction(0.3, 22000L))).isTrue();
        // transaction in the future, to ignore
        assertThat(service.register(new Transaction(1.0, 70000L))).isFalse();
    }

    @Test
    public void testGetStatisticsTimeFrame() {
        // transaction in the past, to ignore
        service.register(new Transaction(0.1, 0));
        // regular transactions
        service.register(new Transaction(0.3, 22000L));
        service.register(new Transaction(0.5, 34000L));
        service.register(new Transaction(0.7, 59000L));
        // transaction in the future, to ignore
        service.register(new Transaction(1.0, 70000L));

        Statistics statistics = service.getStatistics();

        assertEqualStatistics(statistics, new Statistics(1.5, 0.7, 0.3, 3));
    }

    @Test
    public void testGetStatisticsTimePassing() {
        // regular transactions
        service.register(new Transaction(0.3, 22000L));
        service.register(new Transaction(0.5, 34000L));
        service.register(new Transaction(0.7, 59000L));

        // excludes transaction at 22 sec.
        clock.setEpochSecond(82);

        service.register(new Transaction(1.0, 70000L));

        Statistics statistics = service.getStatistics();

        assertEqualStatistics(statistics, new Statistics(2.2, 1.0, 0.5, 3));
    }

    protected void assertEqualStatistics(Statistics statistics, Statistics expected) {
        assertThat(statistics.getCount()).as("check count").isEqualTo(expected.getCount());
        assertThat(statistics.getSum()).as("check sum").isEqualTo(expected.getSum());
        assertThat(statistics.getMin()).as("check min").isEqualTo(expected.getMin());
        assertThat(statistics.getMax()).as("check max").isEqualTo(expected.getMax());
        assertThat(statistics.getAvg()).as("check avg").isEqualTo(expected.getAvg());
    }
}
