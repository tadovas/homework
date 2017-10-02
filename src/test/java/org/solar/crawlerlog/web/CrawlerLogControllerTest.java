package org.solar.crawlerlog.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.solar.crawlerlog.domain.LogId;
import org.solar.crawlerlog.domain.SourceUrl;
import org.solar.crawlerlog.service.CrawlerLogService;
import org.solar.crawlerlog.service.CreationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(CrawlerLogController.class)
public class CrawlerLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrawlerLogService crawlerLogService;

    @Test
    public void shouldCreateNewLogForGivenUrlAndReturnWarningThatAnotherLogWithSameUrlExists() throws Exception {

        ArgumentCaptor<SourceUrl> sourceUrlCaptor = ArgumentCaptor.forClass(SourceUrl.class);
        when(crawlerLogService.createNewCrawlerLog(sourceUrlCaptor.capture())).thenReturn(CreationResult.newResult(LogId.fromString("123") , true));

        mockMvc.perform(
                post("/crawler-logs")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content("{  \"sourceUrl\" : \"test-url\"  }"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location" , equalTo("http://localhost/crawler-logs/123")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.warnings" , hasSize(1)))
                .andExpect(jsonPath("$.warnings[0].code" , is("W01")))
                .andExpect(jsonPath("$.warnings[0].message" , not(isEmptyString())));

        assertThat(sourceUrlCaptor.getValue() , equalTo(SourceUrl.fromString("test-url")));
    }


}