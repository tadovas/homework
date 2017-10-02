package org.solar.crawlerlog.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.solar.crawlerlog.domain.Celebrity;

import javax.validation.Valid;
import java.util.List;

public class AddCelebritiesRequest {

    @JsonProperty
    @Valid
    @NotEmpty
    private List<Celebrity> celebrities;

    public List<Celebrity> getCelebrities() {
        return celebrities;
    }
}
