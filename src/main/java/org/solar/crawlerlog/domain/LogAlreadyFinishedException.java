package org.solar.crawlerlog.domain;

public class LogAlreadyFinishedException extends RuntimeException {

    public LogAlreadyFinishedException(String message) {
        super(message);
    }
}
