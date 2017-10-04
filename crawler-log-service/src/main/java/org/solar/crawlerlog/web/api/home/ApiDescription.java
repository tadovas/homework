package org.solar.crawlerlog.web.api.home;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

class ApiDescription extends ResourceSupport {

  @JsonProperty private String description;

  ApiDescription(String description) {
    this.description = description;
  }
}
