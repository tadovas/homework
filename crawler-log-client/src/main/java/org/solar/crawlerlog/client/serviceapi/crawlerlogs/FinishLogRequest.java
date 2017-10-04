package org.solar.crawlerlog.client.serviceapi.crawlerlogs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishLogRequest {

  @JsonProperty private String repositoryId;

  public FinishLogRequest(String repositoryId) {
    this.repositoryId = repositoryId;
  }
}
