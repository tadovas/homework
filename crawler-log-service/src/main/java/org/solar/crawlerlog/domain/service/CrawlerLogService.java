package org.solar.crawlerlog.domain.service;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import java.util.Collection;
import java.util.function.Consumer;
import org.solar.crawlerlog.domain.model.*;
import org.solar.crawlerlog.domain.repository.CrawlerLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrawlerLogService {

  private CrawlerLogRepository repository;

  @Autowired
  public CrawlerLogService(CrawlerLogRepository crawlerLogRepository) {
    this.repository = crawlerLogRepository;
  }

  @Timed
  @ExceptionMetered
  public CreationResult createNewCrawlerLog(SourceUrl sourceUrl) {

    LogId newLogId = repository.nextLogId();

    boolean anyUnfinishedLogsExist = repository.findAllUnfinishedForSourceUrl(sourceUrl).size() > 0;

    repository.save(CrawlerLog.newCrawlerLog(newLogId, sourceUrl));

    return CreationResult.newResult(newLogId, anyUnfinishedLogsExist);
  }

  @Timed
  @ExceptionMetered
  public void addCelebrities(LogId id, Collection<Celebrity> celebrities) {

    findApplyAndSave(id, crawlerLog -> crawlerLog.addCelebrities(celebrities));
  }

  @Timed
  @ExceptionMetered
  public void finishCrawlerLog(LogId id, RemoteRepositoryId repositoryId) {

    findApplyAndSave(id, crawlerLog -> crawlerLog.finish(repositoryId));
  }

  @Timed
  @ExceptionMetered
  public CrawlerLog findCrawlerLogById(LogId id) {
    return findOrThrowException(id);
  }

  private CrawlerLog findOrThrowException(LogId id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new CrawlerLogNotFoundException("Log not found with id: " + id));
  }

  private void findApplyAndSave(LogId id, Consumer<CrawlerLog> crawlerLogConsumer) {

    CrawlerLog log = findOrThrowException(id);
    crawlerLogConsumer.accept(log);
    repository.save(log);
  }
}
