package stats.controllers;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import stats.services.StatisticsService;

@RunWith(SpringRunner.class)
@RestClientTest(StatisticsService.class)
public class StatisticsControllerTest {

    @Autowired
    private StatisticsService service;

    @Autowired
    private MockRestServiceServer server;

}
