package org.solar.crawlerlog.client.scanner;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Celebrity {

  @JsonProperty private String name;

  @JsonProperty private String occupation;

  public Celebrity(String name, String occupation) {
    this.name = name;
    this.occupation = occupation;
  }
}
