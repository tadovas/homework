package org.solar.crawlerlog.web.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import org.solar.crawlerlog.domain.model.Celebrity;
import org.solar.crawlerlog.domain.model.CrawlerLog;
import org.solar.crawlerlog.domain.model.RemoteRepositoryId;
import org.solar.crawlerlog.web.api.lifecycle.CrawlerLogController;
import org.springframework.hateoas.ResourceSupport;

public class CrawlerLogView extends ResourceSupport {

  @JsonProperty private String id;

  @JsonProperty private String sourceUrl;

  @JsonProperty private Collection<Celebrity> celebrities;

  @JsonProperty private String repository;

  @JsonProperty private boolean finished;

  protected CrawlerLogView(CrawlerLog crawlerLog) {

    this.id = crawlerLog.getId().toString();
    this.sourceUrl = crawlerLog.getSourceUrl().toString();
    this.celebrities = crawlerLog.getCelebrities();
    this.finished = crawlerLog.isFinished();
    this.repository = crawlerLog.getRepositoryId().map(RemoteRepositoryId::toString).orElse(null);
  }

  public static CrawlerLogView fromCrawlerLog(CrawlerLog crawlerLog) {

    CrawlerLogView crawlerLogViewResource = new CrawlerLogView(crawlerLog);

    String logIdAsString = crawlerLog.getId().toString();

    crawlerLogViewResource.add(
        linkTo(methodOn(CrawlerLogController.class).getCrawlerLogById(logIdAsString))
            .withSelfRel());

    if (!crawlerLog.isFinished()) {
      crawlerLogViewResource.add(
          linkTo(methodOn(CrawlerLogController.class).addCelebritiesToLog(logIdAsString, null))
              .withRel("celebrities"));
      crawlerLogViewResource.add(
          linkTo(methodOn(CrawlerLogController.class).finishLog(logIdAsString, null))
              .withRel("repository"));
    }
    return crawlerLogViewResource;
  }
}
