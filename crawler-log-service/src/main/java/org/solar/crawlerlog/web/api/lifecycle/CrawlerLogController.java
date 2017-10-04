package org.solar.crawlerlog.web.api.lifecycle;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import org.hibernate.validator.constraints.NotEmpty;
import org.solar.crawlerlog.domain.model.LogId;
import org.solar.crawlerlog.domain.service.CrawlerLogService;
import org.solar.crawlerlog.domain.service.CreationResult;
import org.solar.crawlerlog.web.api.CrawlerLogView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/crawler-logs", produces = MediaType.APPLICATION_JSON_VALUE)
public class CrawlerLogController {

  private CrawlerLogService crawlerLogService;

  @Autowired
  public CrawlerLogController(CrawlerLogService crawlerLogService) {
    this.crawlerLogService = crawlerLogService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createNewCrawlerLog(@RequestBody @Validated NewLogRequest logRequest)
      throws Exception {

    CreationResult creationResult =
        crawlerLogService.createNewCrawlerLog(logRequest.getSourceUrl());

    URI crawlerLogUri =
        linkTo(
                methodOn(CrawlerLogController.class)
                    .getCrawlerLogById(creationResult.getLogId().toString()))
            .toUri();

    return ResponseEntity.created(crawlerLogUri)
        .body(new Resource<>(new NewLogCreationResponse(creationResult)));
  }

  @GetMapping(path = "/{id}")
  public Resource<CrawlerLogView> getCrawlerLogById(@PathVariable("id") @NotEmpty String id) {
    return CrawlerLogView.fromCrawlerLog(
        crawlerLogService.findCrawlerLogById(LogId.fromString(id)));
  }

  @PostMapping(path = "/{id}/celebrities", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addCelebritiesToLog(
      @PathVariable("id") @NotEmpty String id,
      @RequestBody @Validated AddCelebritiesRequest request) {

    crawlerLogService.addCelebrities(LogId.fromString(id), request.getCelebrities());
    return ResponseEntity.accepted().build();
  }

  @PutMapping(path = "/{id}/repository", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> finishLog(
      @PathVariable("id") @NotEmpty String id, @RequestBody @Validated FinishLogRequest request) {

    crawlerLogService.finishCrawlerLog(LogId.fromString(id), request.getRemoteRepositoryId());
    return ResponseEntity.accepted().build();
  }
}
