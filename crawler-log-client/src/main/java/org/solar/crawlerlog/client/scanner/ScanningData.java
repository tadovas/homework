package org.solar.crawlerlog.client.scanner;

import java.util.*;

public class ScanningData {

    private static Random randomGenerator = new Random();

    private static final List<String> availableUrls = Arrays.asList(
            "www.imdb.com",
            "www.delfi.lt",
            "panele.lt",
            "cosmopolitan.com"
    );

    private static final List<String> availableNames = Arrays.asList(
            "Elton John",
            "Arnold Schwarsznegger",
            "Daffy duck",
            "Sylvester Stalone",
            "Donald duck",
            "Oksana Pikul",
            "Tom and Jerry",
            "Arvydas Sabonis"
    );

    private static final List<String> availableOccupations = Arrays.asList(
            "Actor",
            "Singer",
            "Scandalist",
            "Party tiger",
            "Criminal",
            "Editor",
            "Knows everything",
            "Basketball player",
            "LIDL ads face"
    );

    private static String pickOne(List<String> availabeElements) {
        int index = randomGenerator.nextInt(availabeElements.size());
        return availabeElements.get(index);
    }


    public static Collection<Celebrity> scrappedCelebrities() {
        //avoid zero size lists - crawler log service doesn't like that :)
        int celebritiesToGenerate = randomGenerator.nextInt(10) + 1;

        List<Celebrity> celebrities = new ArrayList<>(celebritiesToGenerate);

        for(int i = 0 ; i < celebritiesToGenerate; i++ ){
            celebrities.add(new Celebrity(
                    pickOne(availableNames),
                    pickOne(availableOccupations)
            ));
        }
        return celebrities;
    }

    public static String nextUrlToScan() {
        return pickOne(availableUrls);
    }

    public static String remoteRepoId() {
        return UUID.randomUUID().toString();
    }
}
