package org.solar.crawlerlog.service;


import org.solar.crawlerlog.domain.LogId;

public class CreationResult {

    private boolean anotherLogExists;

    private LogId createdLogId;

    public CreationResult(LogId createdLogId, boolean anotherLogExists) {

        this.createdLogId = createdLogId;
        this.anotherLogExists =anotherLogExists;
    }



    public static CreationResult newResult( LogId createdLogId , boolean anotherLogExists) {

        return new CreationResult( createdLogId , anotherLogExists);
    }

    public LogId getLogId() {
        return createdLogId;
    }

    public boolean anotherLogExists() {
        return anotherLogExists;
    }
}
