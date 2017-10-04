package org.solar.crawlerlog;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CrawlerLogApplicationHappyPaths {

  @Autowired private TestRestTemplate restClient;

  @Test
  public void crawlerLogFullLifeCycleTest() throws Exception {

    String entryPoint = "/";

    ResponseEntity<Resource<Object>> response = get(entryPoint);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    Link crawlerLogsUri = response.getBody().getLink("crawler-logs");
    Link crawlerSearchLink = response.getBody().getLink("crawler-logs-search");
    assertThat(crawlerLogsUri, notNullValue());
    assertThat(crawlerSearchLink, notNullValue());

    response = post(crawlerLogsUri.getHref(), newCrawlerLogRequest());

    assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    URI createdCrawlerLog = response.getHeaders().getLocation();
    assertThat(createdCrawlerLog, notNullValue());

    response = get(createdCrawlerLog.toString());

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    Link celebritiesSubResource = response.getBody().getLink("celebrities");
    Link repositorySubResource = response.getBody().getLink("repository");
    assertThat(celebritiesSubResource, notNullValue());
    assertThat(repositorySubResource, notNullValue());

    response = post(celebritiesSubResource.getHref(), addCelebritiesRequest());

    assertThat(response.getStatusCode(), equalTo(HttpStatus.ACCEPTED));

    put(repositorySubResource.getHref(), createFinishCrawlerLogRequest());

    response =
        get(
            crawlerSearchLink.getHref() + "?matchUrl={searchValue}",
            UriUtils.encode("www.abc.lt", "UTF-8"));

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
  }

  @Test
  public void metricsEndpointShouldBeAvailable() {

    ResponseEntity<Map> response = restClient.getForEntity("/metrics", Map.class);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(
        (Map<String, Object>) response.getBody(),
        Matchers.hasEntry(equalTo("uptime"), notNullValue()));
  }

  @Test
  public void healthEndpointShouldBeAvailable() {

    ResponseEntity<Map> response = restClient.getForEntity("/health", Map.class);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(
        (Map<String, Object>) response.getBody(),
        Matchers.hasEntry(equalTo("status"), notNullValue()));
  }

  private Object createFinishCrawlerLogRequest() {
    Map<String, String> finishLogRequest = new HashMap<>();
    finishLogRequest.put("repositoryId", "remoteRepoId");
    return finishLogRequest;
  }

  private static Map<String, String> celebrity(String name, String occupation) {
    Map<String, String> map = new HashMap<>();
    map.put("name", name);
    map.put("occupation", occupation);
    return map;
  }

  private static Object addCelebritiesRequest() {
    Map<String, Object> celebritiesRequest = new HashMap<>();

    celebritiesRequest.put(
        "celebrities", Arrays.asList(celebrity("Arnold", "actor"), celebrity("Elton", "singer")));
    return celebritiesRequest;
  }

  private Object newCrawlerLogRequest() {
    HashMap<String, Object> request = new HashMap<>();
    request.put("sourceUrl", "www.abc.lt");
    return request;
  }

  @SuppressWarnings("unchecked")
  private <T extends Resource<?>> ResponseEntity<T> get(String url, Object... args) {
    return restClient.getForEntity(url, (Class) Resource.class, args);
  }

  @SuppressWarnings("unchecked")
  private <T extends Resource<?>> ResponseEntity<T> post(String url, Object request) {
    return restClient.postForEntity(url, request, (Class) Resource.class);
  }

  private void put(String url, Object request) {
    //strange that put action does not return response or at least status code
    restClient.put(url, request);
  }
}
