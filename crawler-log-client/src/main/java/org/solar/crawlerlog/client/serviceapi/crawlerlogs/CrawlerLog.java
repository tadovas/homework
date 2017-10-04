package org.solar.crawlerlog.client.serviceapi.crawlerlogs;

import org.solar.crawlerlog.client.scanner.Celebrity;
import org.springframework.hateoas.Link;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

public class CrawlerLog {

    private String url;

    private RestTemplate restTemplate;

    private CrawlerLogResponse currentState;

    public CrawlerLog(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public void addCelebrities(Collection<Celebrity> celebrities) {
        refresh();

        Link celebritiesSubresource = currentState.getLink("celebrities");

        restTemplate.postForEntity( celebritiesSubresource.getHref() , new AddCelebritiesRequest(celebrities) , Void.class);
        System.out.println(celebrities.size() + " celebrities added to crawlerlog");
    }

    public void finish(String repoId) {
        refresh();

        Link repositorySubresource = currentState.getLink("repository");

        restTemplate.put( repositorySubresource.getHref() , new FinishLogRequest(repoId));
        System.out.println("Crawler log marked as finished with repo: " + repoId);

    }

    //temporal dependency code smell :(
    //actually according to REST principles actions available on resource are provided as links, so refresh our resource
    //just in case
    private void refresh() {
        currentState = restTemplate.getForObject(url , CrawlerLogResponse.class);
    }
}
