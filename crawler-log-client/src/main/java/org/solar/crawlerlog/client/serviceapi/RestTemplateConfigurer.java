package org.solar.crawlerlog.client.serviceapi;

import org.springframework.web.client.RestTemplate;

public class RestTemplateConfigurer {

  public static RestTemplate configure() {
    return new RestTemplate();
  }
}
