package org.solar.crawlerlog.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.solar.crawlerlog.service.CreationResult;
import java.util.Collections;
import java.util.List;

public class NewLogCreationResponse {

    @JsonProperty
    private List<Warning> warnings = Collections.emptyList();

    @JsonProperty
    private String id;

    public NewLogCreationResponse(CreationResult creationResult){
        this.id = creationResult.getLogId().toString();
        if(creationResult.anotherLogExists()){
            warnings = Collections.singletonList(Warning.logAlreadyExists());
        }
    }

}
