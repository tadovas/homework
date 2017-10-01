package org.solar.crawlerlog.domain;


import java.util.Collection;
import java.util.Optional;

public interface CrawlerLogRepository {

    LogId nextLogId();

    CrawlerLog save(CrawlerLog crawlerLog);

    Optional<CrawlerLog> findById(LogId logId);

    Collection<CrawlerLog> findAllUnfinishedForSourceUrl(SourceUrl url);
}
