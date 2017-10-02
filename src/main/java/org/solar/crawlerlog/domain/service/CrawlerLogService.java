package org.solar.crawlerlog.domain.service;


import org.solar.crawlerlog.domain.model.*;
import org.solar.crawlerlog.domain.repository.CrawlerLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Consumer;


@Service
public class CrawlerLogService {

    private CrawlerLogRepository repository;

    @Autowired
    public CrawlerLogService(CrawlerLogRepository crawlerLogRepository) {
        this.repository = crawlerLogRepository;
    }


    public CreationResult createNewCrawlerLog(SourceUrl sourceUrl) {

        LogId newLogId = repository.nextLogId();

        boolean anyUnfinishedLogsExist = repository.findAllUnfinishedForSourceUrl(sourceUrl).size() > 0;

        repository.save(CrawlerLog.newCrawlerLog(newLogId , sourceUrl));

        return CreationResult.newResult( newLogId , anyUnfinishedLogsExist);
    }

    public void addCelebrities(LogId id , Collection<Celebrity> celebrities) {

        findApplyAndSave(id , crawlerLog -> crawlerLog.addCelebrities(celebrities));
    }

    public void finishCrawlerLog(LogId id, RemoteRepositoryId repositoryId) {

        findApplyAndSave(id , crawlerLog -> crawlerLog.finish(repositoryId));
    }


    private CrawlerLog findOrThrowException(LogId id) {
        return repository.findById(id).orElseThrow(() -> new CrawlerLogNotFoundException("Log not found with id: " + id));
    }

    private void findApplyAndSave(LogId id , Consumer<CrawlerLog> crawlerLogConsumer) {

        CrawlerLog log = findOrThrowException(id);
        crawlerLogConsumer.accept(log);
        repository.save(log);
    }

    public CrawlerLog findCrawlerLogById(LogId id) {
        return findOrThrowException(id);
    }
}

