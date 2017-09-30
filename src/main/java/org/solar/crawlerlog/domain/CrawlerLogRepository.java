package org.solar.crawlerlog.domain;


import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface CrawlerLogRepository {

    LogId nextLogId();

    CrawlerLog save(CrawlerLog crawlerLog);

    Stream<CrawlerLog> findAllBySpec(Predicate<CrawlerLog> crawlerLogSpec);

    Optional<CrawlerLog> findById(LogId logId);


}
