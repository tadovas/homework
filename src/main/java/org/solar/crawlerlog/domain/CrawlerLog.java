package org.solar.crawlerlog.domain;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CrawlerLog {

    private LogId id;

    private SourceUrl sourceUrl;

    /**
     * Because of our naive in memory implementation approach, persisted objects are not thread safe and might be shared
     * between threads (return reference not copy). @see {@link org.solar.crawlerlog.repository.ConcurrentHashMapRepository}
     */
    private Collection<Celebrity> celebrityList = new ConcurrentLinkedQueue<>();

    private RepositoryId repositoryId;

    private CrawlerLog(LogId id, SourceUrl sourceUrl) {

        this.id = id;
        this.sourceUrl = sourceUrl;
    }

    public void addCelebrities(Collection<Celebrity> celebrities) {
        celebrityList.addAll(celebrities);
    }

    public void finish(RepositoryId repositoryId) {
        this.repositoryId = repositoryId;
    }

    public boolean isFinished() {
        return repositoryId != null;
    }

    public LogId getId() {
        return id;
    }

    public SourceUrl getSourceUrl() {
        return sourceUrl;
    }

    public static CrawlerLog newCrawlerLog(LogId id , SourceUrl sourceUrl){

        return new CrawlerLog(id , sourceUrl);
    }

}
