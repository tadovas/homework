package org.solar.crawlerlog.domain;

import java.util.function.Predicate;

public final class CrawlerLogPredicates {

    private CrawlerLogPredicates(){}

    public static Predicate<CrawlerLog> hasSourceUrl(SourceUrl url) {
        return (crawlerLog -> crawlerLog.getSourceUrl().equals(url));
    }

    public static Predicate<CrawlerLog> finished(){
        return CrawlerLog::isFinished;
    }

    public static Predicate<CrawlerLog> notFinished(){
        return finished().negate();
    }
}
