package org.solar.crawlerlog.persistence;


import org.junit.Before;
import org.junit.Test;
import org.solar.crawlerlog.domain.CrawlerLog;
import org.solar.crawlerlog.domain.LogId;
import org.solar.crawlerlog.domain.RemoteRepositoryId;
import org.solar.crawlerlog.domain.SourceUrl;

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

        assertThat(result , hasItem( hasProperty("id" , equalTo(LogId.fromString("id1")))));

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
