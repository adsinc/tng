package task.statistics;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class StatisticsService {

    public void addTransaction() {

    }

    @NotNull
    public Statistics getStatistics(long currentTime){
        return null;
    }
}
