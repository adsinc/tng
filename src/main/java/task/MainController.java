package task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import task.statistics.IStatisticsService;
import task.statistics.Statistics;
import task.statistics.Transaction;

import javax.annotation.Resource;
import java.time.Instant;

@RestController
@SpringBootApplication
public class MainController {
    @Resource private IStatisticsService statisticsService;

    @PostMapping("/transactions")
    public ResponseEntity transactions(@RequestBody Transaction transaction) {
        statisticsService.addTransaction(transaction, Instant.now());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity handleException() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Statistics> statistics() {
        return ResponseEntity.ok(
                statisticsService.getLastMinuteStatistics(Instant.now())
        );
    }

    public static void main(String[] args) {
        SpringApplication.run(MainController.class, args);
    }
}
