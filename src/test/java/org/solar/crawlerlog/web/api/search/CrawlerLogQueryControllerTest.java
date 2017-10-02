package org.solar.crawlerlog.web.api.search;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.solar.crawlerlog.domain.model.CrawlerLog;
import org.solar.crawlerlog.domain.model.LogId;
import org.solar.crawlerlog.domain.model.SourceUrl;
import org.solar.crawlerlog.domain.repository.CrawlerLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CrawlerLogQueryController.class)
public class CrawlerLogQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrawlerLogRepository repository;

    @Test
    public void shouldReturnAllUnfinishedLogsFromRepo() throws Exception {

        Mockito.when(repository.findAllUnfinished()).thenReturn(Arrays.asList(
                createNewLog("1") ,
                createNewLog("2")));

        mockMvc.perform(
                get("/crawler-logs/search").param("unfinished" , "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content[*].id" , hasItems("1" , "2")));
    }

    @Test
    public void shouldReturnAllFinishedJobsFromRepo() throws Exception {

        Mockito.when(repository.findAllFinishedWithMatchingSourceUrl(Mockito.any())).thenReturn(Arrays.asList(
                createNewLog("3") ,
                createNewLog("4")));

        String urlToMatch = "http://123.com?query=ab c";

        mockMvc.perform(
                get("/crawler-logs/search")
                        .param("matchUrl" , urlToMatch )
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content[*].id" , hasItems( "3" , "4")));

        verify(repository).findAllFinishedWithMatchingSourceUrl(eq(urlToMatch));
    }

    private static CrawlerLog createNewLog(String id ) {
        return CrawlerLog.newCrawlerLog(LogId.fromString(id) , SourceUrl.fromString("any"));
    }
}