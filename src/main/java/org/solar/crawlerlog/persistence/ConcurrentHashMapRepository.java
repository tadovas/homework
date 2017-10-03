package org.solar.crawlerlog.persistence;

import org.solar.crawlerlog.domain.model.CrawlerLog;
import org.solar.crawlerlog.domain.model.LogId;
import org.solar.crawlerlog.domain.model.SourceUrl;
import org.solar.crawlerlog.domain.repository.CrawlerLogRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.solar.crawlerlog.persistence.CrawlerLogPredicates.*;
import static org.solar.crawlerlog.persistence.CrawlerLogPredicates.hasSourceUrl;
import static org.solar.crawlerlog.persistence.CrawlerLogPredicates.notFinished;


/**
 *   This in-memory persistence implementation approach is a bit naive as we break
 *   semantics of real persistence when saving and returning persisted objects
 *   they are stored and retrieved as references instead of full copies like real persistence storages do.
 *   Therefore objects which we are persisting should be aware of possible concurrent access and it's not very cool :(
 */
@Repository
public class ConcurrentHashMapRepository implements CrawlerLogRepository {

    private Map<LogId, CrawlerLog> crawlerLogMap = new ConcurrentHashMap<>();

    @Override
    public LogId nextLogId() {

        return LogId.fromString(UUID.randomUUID().toString());
    }

    @Override
    public CrawlerLog save(CrawlerLog crawlerLog) {

        return crawlerLogMap.put(crawlerLog.getId() , crawlerLog);
    }

    @Override
    public Optional<CrawlerLog> findById(LogId logId) {

        return Optional.ofNullable(crawlerLogMap.get(logId));
    }

    @Override
    public Collection<CrawlerLog> findAllUnfinishedForSourceUrl(SourceUrl url) {
        return Collections.unmodifiableCollection(
                queryByPredicate(
                        hasSourceUrl(url).and(notFinished())
                )
        );
    }

    @Override
    public Collection<CrawlerLog> findAllUnfinished() {
        return Collections.unmodifiableCollection(
                queryByPredicate(notFinished()));
    }

    @Override
    public Collection<CrawlerLog> findAllFinishedWithMatchingSourceUrl(String value) {
        return Collections.unmodifiableCollection(
                queryByPredicate(sourceUrlContains(value).and(finished())));
    }

    private Collection<CrawlerLog> queryByPredicate(Predicate<CrawlerLog> predicate){
        return crawlerLogMap.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

}
