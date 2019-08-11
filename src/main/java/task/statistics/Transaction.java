package task.statistics;

import lombok.Value;

@Value
public class Transaction {
    long timestamp;
    double amount;
}