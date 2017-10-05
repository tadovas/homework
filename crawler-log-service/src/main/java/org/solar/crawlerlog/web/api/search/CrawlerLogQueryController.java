package org.solar.crawlerlog.web.api.search;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.stream.Collectors;
import org.solar.crawlerlog.domain.repository.CrawlerLogRepository;
import org.solar.crawlerlog.web.api.CrawlerLogView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

@RestController
@RequestMapping(value = "/crawler-logs/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class CrawlerLogQueryController {

  private CrawlerLogRepository repository;

  @Autowired
  public CrawlerLogQueryController(CrawlerLogRepository repository) {
    this.repository = repository;
  }

  @ApiOperation(value = "Find all unfinished crawler logs")
  @ApiImplicitParams(@ApiImplicitParam(name = "unfinished", dataTypeClass = Boolean.class))
  @GetMapping(params = {"unfinished"})
  public Resources<CrawlerLogView> findAllUnfinishedJobs() {

    return new Resources<>(
        repository
            .findAllUnfinished()
            .stream()
            .map(CrawlerLogView::fromCrawlerLog)
            .collect(Collectors.toList()));
  }

  @ApiImplicitParams(@ApiImplicitParam(name = "matchUrl", dataTypeClass = String.class))
  @ApiOperation("Find all finished logs, with sourceUrls matching value")
  @GetMapping(params = {"matchUrl"})
  public Resources<CrawlerLogView> findAllFinishedAndMatching(
      @RequestParam("matchUrl") String value) throws Exception {

    String decodedUrl = UriUtils.decode(value, "UTF-8");

    return new Resources<>(
        repository
            .findAllFinishedWithMatchingSourceUrl(decodedUrl)
            .stream()
            .map(CrawlerLogView::fromCrawlerLog)
            .collect(Collectors.toList()));
  }

  /** Demo purposes only - hidden from api listing */
  @ApiOperation("Dumps everything - no paging :)")
  @GetMapping
  public Resources<CrawlerLogView> dumpAll() throws Exception {

    return new Resources<>(
        repository
            .findAll()
            .stream()
            .map(CrawlerLogView::fromCrawlerLog)
            .collect(Collectors.toList()));
  }
}
