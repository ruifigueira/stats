package stats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import stats.domain.Statistics;
import stats.domain.Transaction;
import stats.services.StatisticsService;

@Controller
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value="/transactions", method=RequestMethod.POST)
    public void postTransaction(Transaction transaction) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(value="/statistics", method=RequestMethod.GET)
    public Statistics getStatistics() {
        throw new UnsupportedOperationException();
    }
}
