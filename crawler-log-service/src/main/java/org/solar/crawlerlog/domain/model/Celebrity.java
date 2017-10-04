package org.solar.crawlerlog.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

public class Celebrity {

  /**
   * Yes, this is intervention from infrastructure to domain model, but since this data object is so
   * thin, to avoid uncessary property copying from api facing classes to domain classes - lets
   * throw a few annotations and we are good to go.
   */
  @JsonProperty @NotBlank private String name;

  @JsonProperty @NotBlank private String occupation;

  //for jackson
  private Celebrity() {}

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
