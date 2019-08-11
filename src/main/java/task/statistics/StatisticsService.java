package task.statistics;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
class StatisticsService implements IStatisticsService {

    @Override
    public void addTransaction(@NotNull Transaction transaction) {

    }

    @Override
    @NotNull
    public Statistics getLastMinuteStatistics(long currentTime) {
        return null;
    }
}
