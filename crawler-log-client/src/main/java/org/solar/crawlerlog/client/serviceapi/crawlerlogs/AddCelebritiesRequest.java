package org.solar.crawlerlog.client.serviceapi.crawlerlogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.solar.crawlerlog.client.scanner.Celebrity;

import java.util.Collection;

public class AddCelebritiesRequest {

    @JsonProperty
    private Collection<Celebrity> celebrities;

    public AddCelebritiesRequest(Collection<Celebrity> celebrities) {
        this.celebrities = celebrities;
    }
}
