package org.solar.crawlerlog.domain.repository;


import org.solar.crawlerlog.domain.model.CrawlerLog;
import org.solar.crawlerlog.domain.model.LogId;
import org.solar.crawlerlog.domain.model.SourceUrl;

import java.util.Collection;
import java.util.Optional;

public interface CrawlerLogRepository {

    LogId nextLogId();

    CrawlerLog save(CrawlerLog crawlerLog);

    Optional<CrawlerLog> findById(LogId logId);

    Collection<CrawlerLog> findAllUnfinishedForSourceUrl(SourceUrl url);
}
