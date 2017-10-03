package org.solar.crawlerlog;

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

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CrawlerLogApplicationHappyPaths {

    @Autowired
    private TestRestTemplate restClient;

    @Test
    public void crawlerLogFullLifeCycleTest() throws Exception {



        ResponseEntity<Resource<Object>> response = restClient.<Resource<String>>getForEntity("/" , (Class)Resource.class);

        assertThat( response.getStatusCode() , equalTo(HttpStatus.OK) );

        Link crawlerLogsUri = response.getBody().getLink("crawler-logs");
        Link crawlerSearchLink  = response.getBody().getLink("crawler-logs-search");
        assertThat( crawlerLogsUri , notNullValue());
        assertThat( crawlerSearchLink , notNullValue());

        response = restClient.postForEntity(crawlerLogsUri.getHref() , newCrawlerLogRequest() , (Class)Resource.class);

        assertThat( response.getStatusCode() , equalTo(HttpStatus.CREATED));
        URI createdCrawlerLog = response.getHeaders().getLocation();
        assertThat( createdCrawlerLog , notNullValue());

        response = restClient.getForEntity(createdCrawlerLog , (Class)Resource.class);

        assertThat( response.getStatusCode() , equalTo(HttpStatus.OK));
        Link celebritiesSubResource = response.getBody().getLink("celebrities");
        Link repositorySubResource = response.getBody().getLink("repository");
        assertThat( celebritiesSubResource , notNullValue());
        assertThat( repositorySubResource , notNullValue());

        response = restClient.postForEntity(celebritiesSubResource.getHref() , addCelebritiesRequest() ,(Class)Resource.class);

        assertThat(response.getStatusCode() , equalTo(HttpStatus.ACCEPTED));

        //strange that put action does not return response or at least status code
        restClient.put(repositorySubResource.getHref() , createFinishCrawlerLogRequest());

        response = restClient.getForEntity(crawlerSearchLink.getHref() + "?matchUrl={searchValue}" , (Class)Resource.class, UriUtils.encode("www.abc.lt" , "UTF-8"));

        assertThat(response.getStatusCode() , equalTo(HttpStatus.OK));
    }

    private Object createFinishCrawlerLogRequest() {
        Map<String , String> finishLogRequest = new HashMap<>();
        finishLogRequest.put("repositoryId" , "remoteRepoId");
        return finishLogRequest;
    }


    private static Map<String , String> celebrity(String name, String occupation) {
        Map<String , String> map = new HashMap<>();
        map.put("name" , name);
        map.put("occupation" , occupation);
        return map;
    }

    private static Object addCelebritiesRequest() {
        Map<String , Object> celebritiesRequest = new HashMap<>();

        celebritiesRequest.put("celebrities" , Arrays.asList(
                celebrity("Arnold" , "actor"),
                celebrity("Elton" , "singer")));
        return celebritiesRequest;
    }

    private Object newCrawlerLogRequest() {
        HashMap<String , Object> request = new HashMap<>();
        request.put("sourceUrl" , "www.abc.lt");
        return request;
    }
}
