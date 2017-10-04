package org.solar.crawlerlog.domain.model;

public class LogAlreadyFinishedException extends RuntimeException {

  public LogAlreadyFinishedException(String message) {
    super(message);
  }
}
