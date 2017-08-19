package stats.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import stats.domain.Statistics;
import stats.services.StatisticsService;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    private static final String MOCK_TRANSACTION_JSON = "{\"amount\":12.3,\"timestamp\":1478192204000}";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatisticsService service;

    @Test
    public void testPostTransactionNoContent() throws Exception {
        when(service.register(Mockito.any())).thenReturn(false);
        mvc.perform(post("/transactions").content(MOCK_TRANSACTION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andExpect(content().string(""));
    }

    @Test
    public void testPostTransactionCreated() throws Exception {
        when(service.register(Mockito.any())).thenReturn(true);
        mvc.perform(post("/transactions").content(MOCK_TRANSACTION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().string(""));
    }

    @Test
    public void testGetStatistics() throws Exception {
        when(service.getStatistics()).thenReturn(new Statistics(1000, 200, 50, 10));
        mvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(1000d))
                .andExpect(jsonPath("$.avg").value(100d))
                .andExpect(jsonPath("$.max").value(200d))
                .andExpect(jsonPath("$.min").value(50d))
                .andExpect(jsonPath("$.count").value(10));
    }

}
