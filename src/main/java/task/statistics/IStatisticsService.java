package task.statistics;

import javax.validation.constraints.NotNull;

/**
 * Transaction statistics server
 */
public interface IStatisticsService {
    /**
     * Add new transaction
     *
     * @param transaction new transaction
     */
    void addTransaction(@NotNull Transaction transaction);

    /**
     * Get statistics for last 60 seconds
     *
     * @param currentTime current time ms
     */
    @NotNull Statistics getLastMinuteStatistics(long currentTime);
}
