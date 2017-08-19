package stats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import stats.domain.Statistics;
import stats.domain.Transaction;
import stats.services.StatisticsService;

@RestController
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value="/transactions", method=RequestMethod.POST)
    public ResponseEntity<Void> postTransaction(@RequestBody Transaction transaction) {
        boolean registered = statisticsService.register(transaction);
        if (registered) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @RequestMapping(value="/statistics", method=RequestMethod.GET)
    public Statistics getStatistics() {
        return statisticsService.getStatistics();
    }
}
