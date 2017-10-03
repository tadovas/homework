package org.solar.crawlerlog.persistence;


import org.junit.Before;
import org.junit.Test;
import org.solar.crawlerlog.domain.model.CrawlerLog;
import org.solar.crawlerlog.domain.model.LogId;
import org.solar.crawlerlog.domain.model.RemoteRepositoryId;
import org.solar.crawlerlog.domain.model.SourceUrl;

import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class ConcurrentHashMapRepositoryQueryTest {

    private ConcurrentHashMapRepository repository;

    @Before
    public void beforeEach() {
        repository = new ConcurrentHashMapRepository();
    }

    @Test
    public void shouldReturnListOfUnfinishedJobsForUrl(){

        repository.save(createUnfinishedLog("id1" , "url1"));
        repository.save(createFinishedLog("id2" , "url1"));
        repository.save(createFinishedLog("id3" , "url2"));

        Collection<CrawlerLog> result = repository.findAllUnfinishedForSourceUrl(SourceUrl.fromString("url1"));

        assertThat(result , contains( hasProperty("id" , equalTo(LogId.fromString("id1")))));

    }

    @Test
    public void shouldReturnAllUnfinishedJobs() {

        repository.save(createFinishedLog("1" , "url1"));
        repository.save(createUnfinishedLog("2" , "url2"));
        repository.save(createUnfinishedLog("3", "url3"));

        Collection<CrawlerLog> result = repository.findAllUnfinished();

        assertThat(result , containsInAnyOrder(
                hasProperty("id" , equalTo(LogId.fromString("2"))),
                hasProperty("id" , equalTo(LogId.fromString("3")))
        ));
    }

    @Test
    public void shouldReturnAllFinishedJobsMatchingUrl() {

        repository.save(createUnfinishedLog("1" , "url2"));
        repository.save(createFinishedLog("2" , "url2"));
        repository.save(createFinishedLog("3", "url3"));

        Collection<CrawlerLog> result = repository.findAllFinishedWithMatchingSourceUrl("rl2");

        assertThat(result , contains(
                hasProperty("id" , equalTo(LogId.fromString("2")))
        ));
    }


    private static CrawlerLog createUnfinishedLog(String id, String sourceUrl) {
        return CrawlerLog.newCrawlerLog(LogId.fromString(id) , SourceUrl.fromString(sourceUrl));
    }

    private static CrawlerLog createFinishedLog(String id, String sourceUrl) {
        CrawlerLog log = createUnfinishedLog(id , sourceUrl);
        log.finish(RemoteRepositoryId.fromString("irrelevant"));
        return log;
    }

}
