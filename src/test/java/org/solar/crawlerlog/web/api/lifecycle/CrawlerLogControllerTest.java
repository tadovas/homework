package org.solar.crawlerlog.web.api.lifecycle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.solar.crawlerlog.domain.model.*;
import org.solar.crawlerlog.domain.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Test
    public void shouldReturnFoundCrawlerLogWithRequiredLinksWhenFoundInRepository() throws Exception {

        ArgumentCaptor<LogId> logIdArgCaptor = ArgumentCaptor.forClass(LogId.class);
        when(crawlerLogService.findCrawlerLogById(logIdArgCaptor.capture())).thenReturn(createTestCrawlerLog());

        mockMvc.perform(
                get("/crawler-logs/{id}" , "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", equalTo("123")))
                .andExpect(jsonPath("finished", equalTo(false) ))
                .andExpect(jsonPath("$.celebrities", hasSize(2)))
                .andExpect(jsonPath("$.celebrities[0].name", equalTo("Arnold")))
                .andExpect(jsonPath("$.celebrities[0].occupation", equalTo("actor")))
                .andExpect(jsonPath("$.links", hasSize(3)))
                .andExpect(jsonPath("$.links[?(@.rel=='self')].href" , hasItem("http://localhost/crawler-logs/123")))
                .andExpect(jsonPath("$.links[?(@.rel=='celebrities')].href" , hasItem("http://localhost/crawler-logs/123/celebrities")))
                .andExpect(jsonPath("$.links[?(@.rel=='repository')].href" , hasItem("http://localhost/crawler-logs/123/repository")));


        assertThat(logIdArgCaptor.getValue(), equalTo(LogId.fromString("123")));
    }

    @Test
    public void shouldAddListOfCelebritiesToExistingLog() throws Exception {

        mockMvc.perform(
                post("/crawler-logs/123/celebrities")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(
                        "{ \"celebrities\" : [ " +
                            "{ \"name\" : \"Arnold\" , \"occupation\" : \"actor\" }," +
                            "{ \"name\" : \"Elton\" , \"occupation\" : \"singer\" }" +
                        "]}"))
                .andExpect(status().isAccepted());

        ArgumentCaptor<List<Celebrity>> celebritiesCaptor = celebrityCaptor();
        ArgumentCaptor<LogId> logIdCaptor = ArgumentCaptor.forClass(LogId.class);
        verify(crawlerLogService).addCelebrities(logIdCaptor.capture() , celebritiesCaptor.capture());

        assertThat(logIdCaptor.getValue() , equalTo(LogId.fromString("123")));
        assertThat(celebritiesCaptor.getValue() , containsInAnyOrder(
                hasProperty("name", equalTo("Arnold")),
                hasProperty("name", equalTo("Elton"))
        ));

    }

    @Test
    public void putRequestToRepositoryResourceShouldFinishCrawlerLog() throws Exception {

        mockMvc.perform(
                put("/crawler-logs/123/repository")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content("{" +
                            " \"repositoryId\" : \"remoteRepoId\" " +
                        "}"))
                .andExpect(status().isAccepted());

        ArgumentCaptor<LogId> logIdCaptor = ArgumentCaptor.forClass(LogId.class);
        ArgumentCaptor<RemoteRepositoryId> repoIdCaptor = ArgumentCaptor.forClass(RemoteRepositoryId.class);
        verify(crawlerLogService).finishCrawlerLog(logIdCaptor.capture() , repoIdCaptor.capture());

        assertThat(logIdCaptor.getValue() , equalTo(LogId.fromString("123")));
        assertThat(repoIdCaptor.getValue() , equalTo(RemoteRepositoryId.fromString("remoteRepoId")));
    }

    @Test
    public void anyActionOnNonExistentLogReturns404Error() throws Exception {

        doThrow(new CrawlerLogNotFoundException("Irrelevant")).when(crawlerLogService).finishCrawlerLog(any(), any());

        mockMvc.perform(
                put("/crawler-logs/123/repository")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"repositoryId\" : \"irrelevant\"}"))
                .andExpect(status().isNotFound());

        verify(crawlerLogService).finishCrawlerLog(any(), any());
    }

    @Test
    public void anyModificationOnFinishedLogShouldProduce409Error() throws Exception {
        doThrow(new LogAlreadyFinishedException("Irrelevant")).when(crawlerLogService).finishCrawlerLog(any(), any());

        mockMvc.perform(
                put("/crawler-logs/123/repository")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"repositoryId\" : \"irrelevant\"}"))
                .andExpect(status().isConflict());

        verify(crawlerLogService).finishCrawlerLog(any(), any());
    }


    /**
     * Unfortunatelly ArgumentCaptor does not support capturing generic lists very nicely,
     * adding a little workaround
     */
    @SuppressWarnings("unchecked")
    private ArgumentCaptor<List<Celebrity>> celebrityCaptor() {
        return ArgumentCaptor.forClass((Class)List.class);
    }

    private CrawlerLog createTestCrawlerLog() {
        CrawlerLog log = CrawlerLog.newCrawlerLog(LogId.fromString("123") , SourceUrl.fromString("test-url"));
        log.addCelebrities(Arrays.asList(
                new Celebrity("Arnold", "actor"),
                new Celebrity("Elton", "singer")));
        return log;
    }
}