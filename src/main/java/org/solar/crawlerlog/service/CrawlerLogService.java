package org.solar.crawlerlog.service;


import org.solar.crawlerlog.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Consumer;

import static org.solar.crawlerlog.domain.CrawlerLogPredicates.*;

@Service
public class CrawlerLogService {

    private CrawlerLogRepository repository;

    @Autowired
    public CrawlerLogService(CrawlerLogRepository crawlerLogRepository) {
        this.repository = crawlerLogRepository;
    }


    public CreationResult createNewCrawlerLog(SourceUrl sourceUrl){

        LogId newLogId = repository.nextLogId();

        boolean anyUnfinishedLogsExist = repository.findAllBySpec(hasSourceUrl(sourceUrl).and(notFinished())).findFirst().isPresent();

        repository.save(CrawlerLog.newCrawlerLog(newLogId , sourceUrl));

        return CreationResult.newResult( newLogId , anyUnfinishedLogsExist);
    }

    public void addFamousPersons(LogId logId , Collection<FamousPerson> famousPeople) {

        repository.findById(logId).ifPresent(applyAndSave(crawlerLog -> crawlerLog.addFamousPersons(famousPeople) ));


    }

    public void finishCrawlerLog(LogId id, RepositoryId repositoryId) {

        repository.findById(id).ifPresent(applyAndSave(crawlerLog -> crawlerLog.finish(repositoryId)));
    }


    private Consumer<CrawlerLog> applyAndSave(Consumer<CrawlerLog> crawlerLogConsumer){
        return crawlerLogConsumer.andThen(repository::save);
    }

}


