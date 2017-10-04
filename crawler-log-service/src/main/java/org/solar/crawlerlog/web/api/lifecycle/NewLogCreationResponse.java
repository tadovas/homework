package org.solar.crawlerlog.web.api.lifecycle;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import org.solar.crawlerlog.domain.service.CreationResult;

public class NewLogCreationResponse {

  @JsonProperty private List<Warning> warnings = Collections.emptyList();

  @JsonProperty private String id;

  public NewLogCreationResponse(CreationResult creationResult) {
    this.id = creationResult.getLogId().toString();
    if (creationResult.anotherLogExists()) {
      warnings = Collections.singletonList(Warning.logAlreadyExists());
    }
  }
}
