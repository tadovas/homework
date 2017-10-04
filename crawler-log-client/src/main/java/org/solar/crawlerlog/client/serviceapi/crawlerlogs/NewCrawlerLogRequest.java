package org.solar.crawlerlog.client.serviceapi.crawlerlogs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewCrawlerLogRequest {

  @JsonProperty private String sourceUrl;

  public NewCrawlerLogRequest(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }
}
