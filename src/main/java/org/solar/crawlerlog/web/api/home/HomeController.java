package org.solar.crawlerlog.web.api.home;


import org.solar.crawlerlog.web.api.lifecycle.CrawlerLogController;
import org.solar.crawlerlog.web.api.search.CrawlerLogQueryController;
import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
public class HomeController {

    @GetMapping(value = "/" , produces = MediaType.APPLICATION_JSON_VALUE )
    public ApiDescription getApiDescription() throws Exception {

        ApiDescription apiDescription = new ApiDescription("Greetings human/machine. Below you will find a list of apis which could fit your needs. Choose wisely");

        apiDescription.add(linkTo(methodOn(CrawlerLogController.class).createNewCrawlerLog(null)).withRel("crawler-logs"));
        apiDescription.add(linkTo(CrawlerLogQueryController.class).withRel("crawler-logs-search"));
        apiDescription.add(BasicLinkBuilder.linkToCurrentMapping().slash("swagger-ui.html").withRel("Swagger-ui-human-eyes-only"));
        return apiDescription;
    }
}
