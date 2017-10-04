package org.solar.crawlerlog.persistence;

import java.util.function.Predicate;
import org.solar.crawlerlog.domain.model.CrawlerLog;
import org.solar.crawlerlog.domain.model.SourceUrl;

final class CrawlerLogPredicates {

  private CrawlerLogPredicates() {}

  static Predicate<CrawlerLog> hasSourceUrl(SourceUrl url) {
    return (crawlerLog -> crawlerLog.getSourceUrl().equalsIgnoringCase(url));
  }

  static Predicate<CrawlerLog> finished() {
    return CrawlerLog::isFinished;
  }

  static Predicate<CrawlerLog> notFinished() {
    return finished().negate();
  }

  static Predicate<CrawlerLog> sourceUrlContains(String value) {
    return crawlerLog -> crawlerLog.getSourceUrl().matchesPartially(value);
  }

  static Predicate<CrawlerLog> everything() {
    return crawlerLog -> true;
  }
}
