package org.solar.crawlerlog.web.api.lifecycle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Warning {

  @JsonProperty private String code;

  @JsonProperty private String message;

  private Warning(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Warning logAlreadyExists() {
    return new Warning("W01", "Log for source url already exists");
  }
}
