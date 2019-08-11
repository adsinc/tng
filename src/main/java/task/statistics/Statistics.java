package task.statistics;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Statistics {
    double sum;
    double max;
    double min;
    long count;

    public double getAvg() {
        return sum / count;
    }

    Statistics(Transaction transaction) {
        sum += transaction.getAmount();
        max = transaction.getAmount();
        min = transaction.getAmount();
        count = 1;
    }

    Statistics update(Transaction transaction) {
        sum += transaction.getAmount();
        max = Math.max(transaction.getAmount(), max);
        min = Math.min(transaction.getAmount(), min);
        count++;
        return this;
    }

    static Statistics merge(Statistics accumulator, Statistics statistics) {
        if (accumulator.count == 0) {
            accumulator.max = statistics.max;
            accumulator.min = statistics.min;
        } else {
            accumulator.max = Math.max(statistics.max, accumulator.max);
            accumulator.min = Math.min(statistics.min, accumulator.min);
        }
        accumulator.sum += statistics.sum;
        accumulator.count += statistics.count;
        return accumulator;
    }

}