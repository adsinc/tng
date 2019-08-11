package task.statistics;

import javax.validation.constraints.NotNull;

/**
 * @author a.dolgiy
 * Date: 11/08/2019 16:15
 */
public interface IStatisticsService {
    void addTransaction();

    @NotNull Statistics getLastMinuteStatistics(long currentTime);
}
