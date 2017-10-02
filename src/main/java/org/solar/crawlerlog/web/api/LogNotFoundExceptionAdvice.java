package org.solar.crawlerlog.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solar.crawlerlog.service.CrawlerLogNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LogNotFoundExceptionAdvice {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(CrawlerLogNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handeException( CrawlerLogNotFoundException exception) {

        logger.error("Caught exception" , exception);
    }
}
