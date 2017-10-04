package org.solar.crawlerlog.client.serviceapi.health;

import org.solar.crawlerlog.client.serviceapi.RecoverableException;
import org.springframework.web.client.RestTemplate;

public class HealthApi {

    private String apiUrl;

    private RestTemplate restTemplate;

    public HealthApi(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
    }

    public void check() {
        HealthResponse resp = restTemplate.getForObject(apiUrl , HealthResponse.class);

        if( !resp.getStatus().equalsIgnoreCase("UP") ) {
            throw new RecoverableException("Service health status is not UP - will try again later");
        }
    }
}
