package task.statistics;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Statistics {
    double sum;
    double avg;
    double max;
    double min;
    long count;
}