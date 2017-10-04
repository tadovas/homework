package org.solar.crawlerlog.client.serviceapi.health;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HealthResponse {

  @JsonProperty private String status;

  public String getStatus() {
    return status;
  }
}
