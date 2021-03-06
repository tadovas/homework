package org.solar.crawlerlog.client;

import java.net.URI;
import org.solar.crawlerlog.client.scanner.ScanningData;
import org.solar.crawlerlog.client.serviceapi.RecoverableException;
import org.solar.crawlerlog.client.serviceapi.apilist.ApiList;
import org.solar.crawlerlog.client.serviceapi.crawlerlogs.CrawlerLog;
import org.solar.crawlerlog.client.serviceapi.crawlerlogs.CrawlerLogs;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

public class CrawlerApplication {

  public static void main(String args[]) throws Exception {
    if (args.length < 1) {
      System.out.println("Bookmark url expected");
      System.exit(-1);
    }

    System.out.println("Url for crawler-log-service access: " + new URI(args[0]));

    Runner.newTask(
            () -> {
              ApiList apiList = ApiList.newApiList(args[0]);

              apiList.getHealthApi().check();

              CrawlerLogs crawlerLogs = apiList.getCrawlerLogsApi();

              CrawlerLog crawlerLog = crawlerLogs.createNew(ScanningData.nextUrlToScan());

              crawlerLog.addCelebrities(ScanningData.scrappedCelebrities());

              crawlerLog.finish(ScanningData.remoteRepoId());
            })
        .continueOn(RecoverableException.class)
        .continueOn(HttpServerErrorException.class)
        .continueOn(ResourceAccessException.class)
        .executeAndSleepSeconds(2)
        .repeatIndefinetely()
        .catchAndLogException()
        .go();
  }
}
