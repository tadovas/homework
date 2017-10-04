package org.solar.crawlerlog.client.serviceapi.crawlerlogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collections;
import java.util.List;

public class CrawlerLogCreationResponse extends ResourceSupport {

    @JsonProperty
    private List<CreationWarning> warnings = Collections.emptyList();

    public void printWarnings() {
        warnings.forEach(System.out::println);
    }
}
