package org.solar.crawlerlog.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.solar.crawlerlog.domain.RemoteRepositoryId;

public class FinishLogRequest {

    @JsonProperty
    @NotBlank
    private String repositoryId;

    public RemoteRepositoryId getSourceUrl() {
        return RemoteRepositoryId.fromString(repositoryId);
    }
}
