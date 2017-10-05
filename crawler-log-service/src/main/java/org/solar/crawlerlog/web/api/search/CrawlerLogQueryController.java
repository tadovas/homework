package org.solar.crawlerlog.web.api.search;

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

  @GetMapping(params = {"unfinished"})
  public Resources<CrawlerLogView> findAllUnfinishedJobs() {

    return new Resources<>(
        repository
            .findAllUnfinished()
            .stream()
            .map(CrawlerLogView::fromCrawlerLog)
            .collect(Collectors.toList()));
  }

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
