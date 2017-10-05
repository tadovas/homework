package org.solar.crawlerlog.web.api.lifecycle;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

  @ApiOperation("Create new crawler log")
  @ApiResponses({
    @ApiResponse(code = 201, message = "Log successfully created"),
    @ApiResponse(code = 400, message = "Request body validation problem")
  })
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

  @ApiOperation("Find crawler log by id")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Log by id is found successfully"),
    @ApiResponse(code = 404, message = "Log by id is not found")
  })
  @GetMapping(path = "/{id}")
  public CrawlerLogView getCrawlerLogById(@PathVariable("id") @NotEmpty String id) {
    return CrawlerLogView.fromCrawlerLog(
        crawlerLogService.findCrawlerLogById(LogId.fromString(id)));
  }

  @ApiOperation("Add celebrities to the log defined by id")
  @ApiResponses({
    @ApiResponse(code = 202, message = "Celebrities added successfully"),
    @ApiResponse(code = 404, message = "Log not found by id"),
    @ApiResponse(code = 400, message = "Request data validation failed"),
    @ApiResponse(
      code = 409,
      message = "Log is already finished, not furher modifications available"
    )
  })
  @PostMapping(path = "/{id}/celebrities", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addCelebritiesToLog(
      @PathVariable("id") @NotEmpty String id,
      @RequestBody @Validated AddCelebritiesRequest request) {

    crawlerLogService.addCelebrities(LogId.fromString(id), request.getCelebrities());
    return ResponseEntity.accepted().build();
  }

  @ApiOperation("Finish log by sending remote repository id")
  @ApiResponses({
    @ApiResponse(code = 202, message = "Log was finished successfully"),
    @ApiResponse(code = 404, message = "Log not found by id"),
    @ApiResponse(code = 400, message = "Request data validation failed"),
    @ApiResponse(
      code = 409,
      message = "Log is already finished, not furher modifications available"
    )
  })
  @PutMapping(path = "/{id}/repository", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> finishLog(
      @PathVariable("id") @NotEmpty String id, @RequestBody @Validated FinishLogRequest request) {

    crawlerLogService.finishCrawlerLog(LogId.fromString(id), request.getRemoteRepositoryId());
    return ResponseEntity.accepted().build();
  }
}
