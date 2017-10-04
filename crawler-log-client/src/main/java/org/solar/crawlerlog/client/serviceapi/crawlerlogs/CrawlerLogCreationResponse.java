package org.solar.crawlerlog.client.serviceapi.crawlerlogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import org.springframework.hateoas.ResourceSupport;

public class CrawlerLogCreationResponse extends ResourceSupport {

  @JsonProperty private List<CreationWarning> warnings = Collections.emptyList();

  public List<CreationWarning> getWarnings() {
    return warnings;
  }
}
