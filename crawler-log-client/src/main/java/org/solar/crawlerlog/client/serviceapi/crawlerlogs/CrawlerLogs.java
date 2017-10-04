package org.solar.crawlerlog.client.serviceapi.crawlerlogs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CrawlerLogs {

  private String url;

  private RestTemplate restTemplate;

  public CrawlerLogs(String url, RestTemplate restTemplate) {
    this.url = url;
    this.restTemplate = restTemplate;
  }

  public CrawlerLog createNew(String sourceUrl) {

    NewCrawlerLogRequest crawlerLogRequest = new NewCrawlerLogRequest(sourceUrl);

    ResponseEntity<CrawlerLogCreationResponse> response =
        restTemplate.postForEntity(url, crawlerLogRequest, CrawlerLogCreationResponse.class);
    System.out.println("Created new CrawlerLog for: " + sourceUrl);

    response.getBody().getWarnings().forEach(System.out::println);

    String createdCrawlerLogUrl = response.getHeaders().getLocation().toString();

    return new CrawlerLog(createdCrawlerLogUrl, restTemplate);
  }
}
