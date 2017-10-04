package org.solar.crawlerlog.client.serviceapi.apilist;

import org.solar.crawlerlog.client.serviceapi.crawlerlogs.CrawlerLogs;
import org.solar.crawlerlog.client.serviceapi.health.HealthApi;
import org.springframework.hateoas.Link;
import org.springframework.web.client.RestTemplate;

public class ApiList {

    private RestTemplate restTemplate;

    private ApiListResponse apiListResponse;

    private ApiList(String uri , RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.apiListResponse =  restTemplate.getForObject(uri , ApiListResponse.class);
        System.out.println("Greetins from service: \n" + apiListResponse.getMessage());
    }

    public static ApiList newApiList(String uri) {

        return new ApiList( uri , new RestTemplate());
    }


    public HealthApi getHealthApi() {

        Link healthLink  = apiListResponse.getLink("health");

        return new HealthApi( healthLink.getHref() , restTemplate);
    }

    public CrawlerLogs getCrawlerLogsApi() {

        Link crawlerLogsLink = apiListResponse.getLink("crawler-logs");

        return new CrawlerLogs(crawlerLogsLink.getHref() , restTemplate);
    }
}
