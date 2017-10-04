package org.solar.crawlerlog.client.serviceapi.crawlerlogs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreationWarning {

    @JsonProperty
    private String code;

    @JsonProperty
    private String message;

    @Override
    public String toString() {
        return "CreationWarning{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
