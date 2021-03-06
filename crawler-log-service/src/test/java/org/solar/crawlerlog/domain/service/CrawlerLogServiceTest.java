package org.solar.crawlerlog.domain.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.solar.crawlerlog.domain.model.*;
import org.solar.crawlerlog.domain.repository.CrawlerLogRepository;

public class CrawlerLogServiceTest {

  private CrawlerLogService crawlerLogService;

  private CrawlerLogRepository crawlerLogRepository = mock(CrawlerLogRepository.class);

  @Before
  public void beforeEach() {

    crawlerLogService = new CrawlerLogService(crawlerLogRepository);
  }

  @Test
  public void createNewLogReturnsLogResultContainingLogId() {

    when(crawlerLogRepository.nextLogId()).thenReturn(LogId.fromString("123"));
    when(crawlerLogRepository.findAllUnfinishedForSourceUrl(any()))
        .thenReturn(Collections.emptyList());

    CreationResult result =
        crawlerLogService.createNewCrawlerLog(SourceUrl.fromString("urlToScan"));

    assertThat(result.getLogId(), equalTo(LogId.fromString("123")));
    assertThat(result.anotherLogExists(), equalTo(false));

    ArgumentCaptor<CrawlerLog> capturedLog = ArgumentCaptor.forClass(CrawlerLog.class);
    verify(crawlerLogRepository).save(capturedLog.capture());

    assertThat(
        capturedLog.getValue(),
        allOf(
            hasProperty("id", equalTo(LogId.fromString("123"))),
            hasProperty("sourceUrl", equalTo(SourceUrl.fromString("urlToScan")))));
  }

  @Test
  public void createNewLogReturnsFlagSetIfLogWithSourceUrlAlreadyExists() {

    when(crawlerLogRepository.nextLogId()).thenReturn(LogId.fromString("irrelevant"));
    when(crawlerLogRepository.findAllUnfinishedForSourceUrl(any()))
        .thenReturn(
            Arrays.asList(
                CrawlerLog.newCrawlerLog(LogId.fromString("any"), SourceUrl.fromString("any"))));

    CreationResult result = crawlerLogService.createNewCrawlerLog(SourceUrl.fromString("any"));

    assertThat(result.anotherLogExists(), equalTo(true));
  }

  @Test
  public void addsListOfFamousPeopleToLog() {

    CrawlerLog existingLog = mock(CrawlerLog.class);
    when(crawlerLogRepository.findById(any())).thenReturn(Optional.ofNullable(existingLog));

    Collection<Celebrity> famousPeople = Collections.emptyList();
    crawlerLogService.addCelebrities(LogId.fromString("irrelevant"), famousPeople);

    verify(crawlerLogRepository).save(eq(existingLog));

    verify(existingLog).addCelebrities(eq(famousPeople));
  }

  @Test
  public void marksCrawlerLogAsCompleted() {

    CrawlerLog existingCrawlerLog = mock(CrawlerLog.class);
    when(crawlerLogRepository.findById(any())).thenReturn(Optional.ofNullable(existingCrawlerLog));

    RemoteRepositoryId repositoryId = RemoteRepositoryId.fromString("RepoId");
    crawlerLogService.finishCrawlerLog(LogId.fromString("irrelevant"), repositoryId);

    verify(crawlerLogRepository).save(eq(existingCrawlerLog));

    verify(existingCrawlerLog).finish(eq(repositoryId));
  }

  @Test(expected = CrawlerLogNotFoundException.class)
  public void logNotFoundExceptionShouldBeThrownIfTryingToFinishNonExistingLog() {

    when(crawlerLogRepository.findById(any())).thenReturn(Optional.empty());

    crawlerLogService.finishCrawlerLog(
        LogId.fromString("irrelevant"), RemoteRepositoryId.fromString("irrelevant"));

    fail("CrawlerLogNotFoundException should be thrown");
  }
}
