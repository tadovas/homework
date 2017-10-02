package org.solar.crawlerlog.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.solar.crawlerlog.domain.Celebrity;
import org.solar.crawlerlog.domain.CrawlerLog;
import org.solar.crawlerlog.domain.RemoteRepositoryId;
import org.springframework.hateoas.Resource;

import java.util.Collection;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class CrawlerLogView {

    @JsonProperty
    private Collection<Celebrity> celebrities;

    @JsonProperty
    private String repository;

    @JsonProperty
    private boolean finished;

    private CrawlerLogView(CrawlerLog crawlerLog) {

        this.celebrities = crawlerLog.getCelebrities();
        this.finished = crawlerLog.isFinished();
        this.repository = crawlerLog.getRepositoryId().map(RemoteRepositoryId::toString).orElse(null);
    }

    public static Resource<CrawlerLogView> fromCrawlerLog(CrawlerLog crawlerLog) {

        Resource<CrawlerLogView> crawlerLogViewResource = new Resource<>(new CrawlerLogView(crawlerLog));

        crawlerLogViewResource.add(linkTo(methodOn(CrawlerLogController.class).getCrawlerLogById(crawlerLog.getId().toString())).withSelfRel());

        if(! crawlerLog.isFinished()) {

        }
        return crawlerLogViewResource;
    }

}
