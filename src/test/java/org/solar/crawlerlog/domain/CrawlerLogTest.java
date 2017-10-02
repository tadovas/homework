package org.solar.crawlerlog.domain;

import com.github.npathai.hamcrestopt.OptionalMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CrawlerLogTest {

    private CrawlerLog crawlerLog;

    @Before
    public void beforeEach() {

        crawlerLog = CrawlerLog.newCrawlerLog(LogId.fromString("id") , SourceUrl.fromString("url"));
    }

    @Test
    public void idAndSourceUrlShouldBePersisted() {

        assertThat(crawlerLog , allOf(
                hasProperty("id" , equalTo(LogId.fromString("id"))),
                hasProperty("sourceUrl" , equalTo(SourceUrl.fromString("url")))
        ));
    }

    @Test
    public void celebrityListShouldBePersisted() {

        Celebrity celeb1 = new Celebrity("Arnold", "actor");
        Celebrity celeb2 = new Celebrity("Elton", "singer");

        crawlerLog.addCelebrities(Arrays.asList(celeb1));
        crawlerLog.addCelebrities(Arrays.asList(celeb2));

        assertThat(crawlerLog.getCelebrities() ,
                hasItems( celeb1 , celeb2)
        );
    }

    @Test
    public void crawlerLogShoudBeFinished() {

        crawlerLog.finish(RemoteRepositoryId.fromString("repoId"));

        assertThat(crawlerLog.isFinished() , equalTo(true));
        assertThat(crawlerLog.getRepositoryId() , isPresentAnd(equalTo(RemoteRepositoryId.fromString("repoId"))));
    }

    @Test(expected = LogAlreadyFinishedException.class)
    public void shouldThrowLogAlreadyClosedExceptionWhenAddingCelebsOnClosedLog() {

        crawlerLog.finish(RemoteRepositoryId.fromString("repoId"));
        crawlerLog.addCelebrities(Arrays.asList(new Celebrity("Arnold", "actor")));

        fail("LogAlreadyFinishedException expected");
    }

    @Test(expected = LogAlreadyFinishedException.class)
    public void shouldThrowLogAlreadyFinishedExceptionWhenFinishingAlreadyClosedLog() {

        crawlerLog.finish(RemoteRepositoryId.fromString("repoId"));
        crawlerLog.finish(RemoteRepositoryId.fromString("repoId"));

        fail("LogAlreadyFinishedException expected");
    }
}