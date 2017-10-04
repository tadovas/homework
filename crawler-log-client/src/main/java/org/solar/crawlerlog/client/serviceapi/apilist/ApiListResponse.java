package org.solar.crawlerlog.client.serviceapi.apilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class ApiListResponse extends ResourceSupport {

  @JsonProperty private String description;

  public String getMessage() {
    return description;
  }
}
