package org.solar.crawlerlog.persistence;

import org.solar.crawlerlog.domain.model.CrawlerLog;
import org.solar.crawlerlog.domain.model.SourceUrl;

import java.util.function.Predicate;

final class CrawlerLogPredicates {

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
