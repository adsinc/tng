package task.statistics;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class StatisticsServiceTest {

    private IStatisticsService statisticsService;

    @Before
    public void setUp() {
        statisticsService = new StatisticsService();
    }

    @Test
    public void testStoredLengthInSingleThread() {
        Instant time = Instant.parse("2019-01-01T00:00:01.021Z");
        int transactionsCount = 60;
        IntStream.range(0, transactionsCount)
                .map(i -> transactionsCount - i)
                .mapToObj(i -> new Transaction(time.toEpochMilli() - TimeUnit.SECONDS.toMillis(i), 1))
                .forEach(tr -> statisticsService.addTransaction(tr, time));
        assertEquals(createStatistics(60, 1, 1, 60), statisticsService.getLastMinuteStatistics(time));
        assertEquals(createStatistics(59, 1, 1, 59), statisticsService.getLastMinuteStatistics(time.plus(1, ChronoUnit.SECONDS)));
    }

    @Test
    public void testCalculatedStatInSingleThread() {
        Instant time = Instant.parse("2019-01-01T00:00:01.021Z");
        statisticsService.addTransaction(new Transaction(time.toEpochMilli(), 10), time);
        time = Instant.parse("2019-01-01T00:00:02.921Z");
        statisticsService.addTransaction(new Transaction(time.toEpochMilli(), 20), time);
        time = Instant.parse("2019-01-01T00:00:03.000Z");
        assertEquals(createStatistics(2, 20, 10, 30), statisticsService.getLastMinuteStatistics(time));

        time = Instant.parse("2019-01-01T00:01:01.001Z");
        statisticsService.addTransaction(new Transaction(time.toEpochMilli(), 30), time);
        assertEquals(createStatistics(2, 20, 10, 30), statisticsService.getLastMinuteStatistics(time));
        time = Instant.parse("2019-01-01T00:01:02.001Z");
        assertEquals(createStatistics(2, 30, 20, 50), statisticsService.getLastMinuteStatistics(time));
    }

    @Test
    public void testConcurrentTransactions() {
        Instant time = Instant.parse("2019-01-01T00:00:01.021Z");
        concurrentAddTransactions(6, 10000, cnt -> time.minus(cnt, ChronoUnit.MILLIS));
        assertEquals(
                createStatistics(60000, 6, 1, 210000),
                statisticsService.getLastMinuteStatistics(time.plus(1, ChronoUnit.SECONDS))
        );
    }

    @Test
    public void testConcurrentTransactionsWithCleanUp() {
        Instant time = Instant.parse("2019-01-01T00:00:01.021Z");
        concurrentAddTransactions(6, 100, cnt -> time.minus(cnt, ChronoUnit.MINUTES));
        assertEquals(
                createStatistics(6, 6, 1, 21),
                statisticsService.getLastMinuteStatistics(time)
        );
    }

    private void concurrentAddTransactions(int threadCount, int transactionPerThread, IntFunction<Instant> getTime) {
        CompletableFuture[] futures = IntStream.range(0, threadCount)
                .mapToObj(i -> CompletableFuture.runAsync(() ->
                        IntStream.range(0, transactionPerThread)
                                .map(n -> transactionPerThread - n)
                                .forEach(cnt -> {
                                    Instant t = getTime.apply(cnt);
                                    statisticsService.addTransaction(new Transaction(t.toEpochMilli(), i + 1), t);
                                })
                ))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
    }

    private Statistics createStatistics(int count, int max, int min, double sum) {
        Statistics statistics = new Statistics();
        statistics.setCount(count);
        statistics.setMax(max);
        statistics.setMin(min);
        statistics.setSum(sum);
        return statistics;
    }
}