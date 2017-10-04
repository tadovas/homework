package org.solar.crawlerlog.persistence;

import org.solar.crawlerlog.domain.model.CrawlerLog;
import org.solar.crawlerlog.domain.model.SourceUrl;

import java.util.function.Predicate;

final class CrawlerLogPredicates {

    private CrawlerLogPredicates(){}

    static Predicate<CrawlerLog> hasSourceUrl(SourceUrl url) {
        return (crawlerLog -> crawlerLog.getSourceUrl().equalsIgnoringCase(url));
    }

    static Predicate<CrawlerLog> finished(){
        return CrawlerLog::isFinished;
    }

    static Predicate<CrawlerLog> notFinished(){
        return finished().negate();
    }

    static Predicate<CrawlerLog> sourceUrlContains(String value) {
        return crawlerLog -> crawlerLog.getSourceUrl().matchesPartially(value);
    }

    static Predicate<CrawlerLog> everything() {
        return crawlerLog -> true;
    }
}
