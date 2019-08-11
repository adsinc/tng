package task.statistics;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

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

    Statistics update(Transaction transaction) {
        sum += transaction.getAmount();
        max = Math.max(transaction.getAmount(), max);
        min = Math.min(transaction.getAmount(), min);
        count++;
        return this;
    }

    Statistics merge(Statistics statistics) {
        sum += statistics.getSum();
        max = Math.max(statistics.getMax(), max);
        min = Math.min(statistics.getMin(), min);
        count += statistics.getCount();
        return this;
    }

}