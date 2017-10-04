package org.solar.crawlerlog.web.api.lifecycle;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;
import org.hibernate.validator.constraints.NotEmpty;
import org.solar.crawlerlog.domain.model.Celebrity;

public class AddCelebritiesRequest {

  @JsonProperty @Valid @NotEmpty private List<Celebrity> celebrities;

  public List<Celebrity> getCelebrities() {
    return celebrities;
  }
}
