package org.solar.crawlerlog.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CrawlerLogTest {

    private CrawlerLog crawlerLog;

    @Before
    public void beforeEach() {

        crawlerLog = CrawlerLog.newCrawlerLog(LogId.newLogId("id") , SourceUrl.newUrl("url"));
    }

    @Test
    public void idAndSourceUrlShouldBePersisted() {

        assertThat(crawlerLog , allOf(
                hasProperty("id" , equalTo(LogId.newLogId("id"))),
                hasProperty("sourceUrl" , equalTo(SourceUrl.newUrl("url")))
        ));
    }

    @Test
    public void celebrityListShouldBePersisted() {

        Celebrity celeb1 = new Celebrity();
        Celebrity celeb2 = new Celebrity();

        crawlerLog.addCelebrities(Arrays.asList(celeb1));
        crawlerLog.addCelebrities(Arrays.asList(celeb2));

        assertThat(crawlerLog.getCelebrities() ,
                hasItems( celeb1 , celeb2)
        );
    }

    @Test
    public void crawlerLogShoudBeFinished() {

        crawlerLog.finish(RepositoryId.newId("repoId"));

        assertThat(crawlerLog.isFinished() , equalTo(true));
        assertThat(crawlerLog.getRepositoryId() , equalTo(RepositoryId.newId("repoId")));
    }

    @Test(expected = LogAlreadyFinishedException.class)
    public void shouldThrowLogAlreadyClosedExceptionWhenAddingCelebsOnClosedLog() {

        crawlerLog.finish(RepositoryId.newId("repoId"));
        crawlerLog.addCelebrities(Arrays.asList(new Celebrity()));

        fail("LogAlreadyFinishedException expected");
    }

    @Test(expected = LogAlreadyFinishedException.class)
    public void shouldThrowLogAlreadyFinishedExceptionWhenFinishingAlreadyClosedLog() {

        crawlerLog.finish(RepositoryId.newId("repoId"));
        crawlerLog.finish(RepositoryId.newId("repoId"));

        fail("LogAlreadyFinishedException expected");
    }
}