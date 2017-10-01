package org.solar.crawlerlog.service;

public class CrawlerLogNotFoundException extends RuntimeException{

    public CrawlerLogNotFoundException(String message) {
        super(message);
    }
}
