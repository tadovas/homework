package org.solar.crawlerlog.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CrawlerLog {

    private LogId id;

    private SourceUrl sourceUrl;

    /**
     * Because of our naive in memory implementation approach, persisted objects are not thread safe and might be shared
     * between threads (return reference not copy). @see {@link org.solar.crawlerlog.repository.ConcurrentHashMapRepository}
     */
    private Collection<Celebrity> celebrityList = new ConcurrentLinkedQueue<>();

    /**
     * Same as above - muating reference
     */
    private volatile RepositoryId repositoryId = null;

    private CrawlerLog(LogId id, SourceUrl sourceUrl) {

        this.id = id;
        this.sourceUrl = sourceUrl;
    }

    public boolean isFinished() {
        return repositoryId != null;
    }

    private void ensureNotFinished() {

        if(isFinished()) {
            throw new LogAlreadyFinishedException("Log is already finished: " + id + " repo: " + repositoryId);
        }
    }

    public void addCelebrities(Collection<Celebrity> celebrities) {

        ensureNotFinished();
        celebrityList.addAll(celebrities);
    }

    public void finish(RepositoryId repositoryId) {

        ensureNotFinished();
        this.repositoryId = repositoryId;
    }

    public LogId getId() {
        return id;
    }

    public SourceUrl getSourceUrl() {
        return sourceUrl;
    }

    public RepositoryId getRepositoryId() {
        return repositoryId;
    }

    public Collection<Celebrity> getCelebrities() {
        return Collections.unmodifiableCollection(celebrityList);
    }

    public static CrawlerLog newCrawlerLog(LogId id , SourceUrl sourceUrl){

        return new CrawlerLog(id , sourceUrl);
    }

}
