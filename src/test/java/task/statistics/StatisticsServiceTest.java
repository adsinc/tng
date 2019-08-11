package task.statistics;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class StatisticsServiceTest {

    private IStatisticsService statisticsService;

    @Before
    public void setUp() throws Exception {
        statisticsService = new StatisticsService();
    }

    @Test
    public void testAddTransactionInSingleThread() {
        Instant time = Instant.parse("2019-01-01T00:00:01.021Z");
        int transactionsCount = 60;
        IntStream.rangeClosed(0, transactionsCount)
                .map(i -> transactionsCount - i)
                .mapToObj(i -> new Transaction(time.toEpochMilli() - TimeUnit.SECONDS.toMillis(i), 1))
                .forEach(tr -> statisticsService.addTransaction(tr, time));
        assertEquals(createStatistics(60, 1, 1, 60), statisticsService.getLastMinuteStatistics(time));
    }

    private Statistics createStatistics(int count, int max, int min, double sum) {
        Statistics statistics = new Statistics();
        statistics.setCount(count);
        statistics.setMax(max);
        statistics.setMin(min);
        statistics.setSum(sum);
        return statistics;
    }

    @Test
    public void testGetLastMinuteStatistics() {
    }
}