package org.solar.crawlerlog.web;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.solar.crawlerlog.domain.SourceUrl;

public class NewLogRequest {

    @NotEmpty
    @JsonProperty("sourceUrl")
    private String sourceUrl;

    public SourceUrl getSourceUrl() {
        return SourceUrl.fromString(sourceUrl);
    }
}
