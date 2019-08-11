package task.statistics;

import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Transaction statistics server
 */
public interface IStatisticsService {
    /**
     * Add new transaction
     *
     * @param transaction new transaction
     * @param currentTime current time
     */
    void addTransaction(@NotNull Transaction transaction, @NotNull Instant currentTime);

    /**
     * Get statistics for last 60 seconds
     *
     * @param currentTime current time ms
     */
    @NotNull Statistics getLastMinuteStatistics(Instant currentTime);

}
