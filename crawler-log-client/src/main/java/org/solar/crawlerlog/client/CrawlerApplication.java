package org.solar.crawlerlog.client;

import java.net.URI;

public class CrawlerApplication {

    public static void main(String args[]) throws Exception {
        if(args.length < 1) {
            System.out.println("Bookmark url expected");
            System.exit(-1);
        }

        System.out.println("Url for crawler-log-service access: " + new URI(args[0]));

        Runner.newTask(() -> {

            throw new RuntimeException("I failed - hard");

        }).continueOn(RecoverableException.class)
                .sleepForSeconds(2)
                .repeatIndefinetely()
                .logException()
                .go();


    }
}
