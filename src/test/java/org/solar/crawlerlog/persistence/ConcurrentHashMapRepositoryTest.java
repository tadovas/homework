package org.solar.crawlerlog.persistence;

import org.junit.Before;
import org.junit.Test;
import org.solar.crawlerlog.domain.model.CrawlerLog;
import org.solar.crawlerlog.domain.model.LogId;
import org.solar.crawlerlog.domain.model.SourceUrl;

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

        concurrentHashMapRepository.save(CrawlerLog.newCrawlerLog(LogId.fromString("id1") , SourceUrl.fromString("url1")));
        concurrentHashMapRepository.save(CrawlerLog.newCrawlerLog(LogId.fromString("id2") , SourceUrl.fromString("url2")));

        Optional<CrawlerLog> foundLog = concurrentHashMapRepository.findById(LogId.fromString("id1"));

        assertThat(foundLog , isPresentAnd( allOf(
                hasProperty("id" , equalTo(LogId.fromString("id1"))),
                hasProperty("sourceUrl" , equalTo(SourceUrl.fromString("url1")))
        )));
    }

}