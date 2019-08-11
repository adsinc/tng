package task.statistics;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
class StatisticsService implements IStatisticsService {
    private final ConcurrentHashMap<Instant, Statistics> lastMinuteStat = new ConcurrentHashMap<>();

    @Override
    public void addTransaction(@NotNull Transaction transaction, @NotNull Instant currentTime) {
        Instant time = trunkTime(Instant.ofEpochMilli(transaction.getTimestamp()));
        lastMinuteStat.compute(time, (key, value) -> computeStatistics(value, transaction));
        tryRemoveOldValues(currentTime);
    }

    private Statistics computeStatistics(Statistics statistics, @NotNull Transaction transaction) {
        return (statistics != null ? statistics : new Statistics()).update(transaction);
    }

    private void tryRemoveOldValues(Instant currentTime) {
        Instant lastStoredTime = trunkTime(currentTime).minus(1, ChronoUnit.MINUTES);
        lastMinuteStat.keySet().stream()
                .filter(time -> time.compareTo(lastStoredTime) <= 0)
                .forEach(lastMinuteStat::remove);
    }

    private Instant trunkTime(Instant time) {
        return time.truncatedTo(ChronoUnit.SECONDS);
    }

    @Override
    @NotNull
    public Statistics getLastMinuteStatistics(Instant currentTime) {
        Instant maxTime = trunkTime(currentTime).minus(1, ChronoUnit.SECONDS);
        return lastMinuteStat.entrySet().stream()
                .filter(e -> e.getKey().compareTo(maxTime) <= 0)
                .map(Map.Entry::getValue)
                .reduce(new Statistics(), Statistics::merge);
    }
}
