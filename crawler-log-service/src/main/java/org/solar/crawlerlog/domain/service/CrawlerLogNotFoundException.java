package org.solar.crawlerlog.domain.service;

public class CrawlerLogNotFoundException extends RuntimeException {

  public CrawlerLogNotFoundException(String message) {
    super(message);
  }
}
