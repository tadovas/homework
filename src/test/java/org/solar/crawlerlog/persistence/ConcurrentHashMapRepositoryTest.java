package org.solar.crawlerlog.persistence;

import org.junit.Before;
import org.junit.Test;
import org.solar.crawlerlog.domain.CrawlerLog;
import org.solar.crawlerlog.domain.LogId;
import org.solar.crawlerlog.domain.SourceUrl;

import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ConcurrentHashMapRepositoryTest {

    private ConcurrentHashMapRepository concurrentHashMapRepository;

    @Before
    public void beforeEach(){

        concurrentHashMapRepository = new ConcurrentHashMapRepository();
    }


    @Test
    public void twoGeneratedIdsShouldBeDifferent(){

        LogId logId = concurrentHashMapRepository.nextLogId();
        LogId another = concurrentHashMapRepository.nextLogId();

        assertThat( logId , not(equalTo(another)));
    }

    @Test
    public void savedCrawlerLogShouldBeFoundById(){

        concurrentHashMapRepository.save(CrawlerLog.newCrawlerLog(LogId.newLogId("id1") , SourceUrl.newUrl("url1")));
        concurrentHashMapRepository.save(CrawlerLog.newCrawlerLog(LogId.newLogId("id2") , SourceUrl.newUrl("url2")));

        Optional<CrawlerLog> foundLog = concurrentHashMapRepository.findById(LogId.newLogId("id1"));

        assertThat(foundLog , isPresentAnd( allOf(
                hasProperty("id" , equalTo(LogId.newLogId("id1"))),
                hasProperty("sourceUrl" , equalTo(SourceUrl.newUrl("url1")))
        )));
    }

}