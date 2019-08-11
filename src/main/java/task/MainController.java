package task;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import task.statistics.StatisticsService;

import javax.annotation.Resource;

@RestController
@EnableAutoConfiguration
public class MainController {
    // todo interface
    @Resource private StatisticsService statisticsService;

    @PostMapping("/transactions")
    public ResponseEntity transactions(@RequestBody Transaction transaction) {
        statisticsService.
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity handleException() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Statistics> statistics() {
        return ResponseEntity.ok(new Statistics());
    }

    @Value
    private static class Transaction {
        long timestamp;
        double amount;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class Statistics {
        double sum;
        double avg;
        double max;
        double min;
        long count;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainController.class, args);
    }
}
