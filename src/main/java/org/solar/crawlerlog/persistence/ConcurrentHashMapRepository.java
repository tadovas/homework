package org.solar.crawlerlog.persistence;

import org.solar.crawlerlog.domain.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;


import static org.solar.crawlerlog.domain.CrawlerLogPredicates.*;


/**
 *   This in-memory persistence implementation approach is a bit naive as we break
 *   semantics of real persistence when saving and returning persisted objects
 *   they are stored and retrieved as references instead of full copies like real persistence storages do.
 *   Therefore objects which we are persisting should be aware of possible concurrent access and it's not very cool :(
 */
public class ConcurrentHashMapRepository implements CrawlerLogRepository {

    private Map<LogId , CrawlerLog> crawlerLogMap = new ConcurrentHashMap<>();

    @Override
    public LogId nextLogId() {

        return LogId.newLogId(UUID.randomUUID().toString());
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

    private Collection<CrawlerLog> queryByPredicate(Predicate<CrawlerLog> predicate){
        return crawlerLogMap.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

}
