package org.solar.crawlerlog.client.scanner;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public class ScanningData {

    public static Collection<Celebrity> scrappedCelebrities() {
        return Arrays.asList(
                new Celebrity("Arnold" , "actor"),
                new Celebrity("Elton" , "singer")
        );
    }

    public static String nextUrlToScan() {
        return "wwww.abc.lt";
    }

    public static String remoteRepoId() {
        return UUID.randomUUID().toString();
    }
}
