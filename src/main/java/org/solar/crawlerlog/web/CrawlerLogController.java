package org.solar.crawlerlog.web;

import org.solar.crawlerlog.domain.LogId;
import org.solar.crawlerlog.service.CrawlerLogService;
import org.solar.crawlerlog.service.CreationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
@RequestMapping(value = "/crawler-logs" ,
        produces = MediaType.APPLICATION_JSON_VALUE ,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class CrawlerLogController {

    private CrawlerLogService crawlerLogService;

    @Autowired
    public CrawlerLogController(CrawlerLogService crawlerLogService) {
        this.crawlerLogService = crawlerLogService;
    }

    @PostMapping
    public ResponseEntity<?> createNewCrawlerLog(@RequestBody @Validated NewLogRequest logRequest) throws Exception {

        CreationResult creationResult = crawlerLogService.createNewCrawlerLog(logRequest.getSourceUrl());

        URI crawlerLogUri = linkTo(methodOn(CrawlerLogController.class).getCrawlerLogById(creationResult.getLogId().toString())).toUri();

        return ResponseEntity.created(crawlerLogUri).body( new Resource<>(new NewLogCreationResponse(creationResult)));
    }

    @GetMapping(path = "/{id}")
    public Resource<CrawlerLogView> getCrawlerLogById(@PathVariable("id")  String id) {
        return CrawlerLogView.fromCrawlerLog(crawlerLogService.findCrawlerLogById(LogId.fromString(id)));
    }

}
