package org.solar.crawlerlog.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class Celebrity {

    /**
     * Yes, this is intervention from infrastructure to domain model, but since this data object is so thin, to avoid
     * uncessary property copying from api facing classes to domain classes - lets throw a few annotations and we are good
     * to go.
     */

    @JsonProperty
    @NotEmpty
    private String name;

    @JsonProperty
    @NotEmpty
    private String occupation;


    public Celebrity(String name, String occupation) {
        this.name = name;
        this.occupation = occupation;
    }

    public String getName() {
        return name;
    }

    public String getOccupation() {
        return occupation;
    }
}
