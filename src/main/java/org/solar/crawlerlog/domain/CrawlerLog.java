package org.solar.crawlerlog.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CrawlerLog {

    private LogId id;

    private SourceUrl sourceUrl;

    private List<FamousPerson> personList = Collections.emptyList();

    private RepositoryId repositoryId;

    private CrawlerLog(LogId id, SourceUrl sourceUrl) {

        this.id = id;
        this.sourceUrl = sourceUrl;
    }

    public void addFamousPersons(Collection<FamousPerson> personsToAdd) {

    }


    public void finish(RepositoryId repositoryId) {

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
