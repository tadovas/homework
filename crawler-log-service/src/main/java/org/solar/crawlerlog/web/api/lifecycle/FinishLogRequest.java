package org.solar.crawlerlog.web.api.lifecycle;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.solar.crawlerlog.domain.model.RemoteRepositoryId;

public class FinishLogRequest {

  @JsonProperty @NotBlank private String repositoryId;

  public RemoteRepositoryId getRemoteRepositoryId() {
    return RemoteRepositoryId.fromString(repositoryId);
  }
}
