package org.solar.crawlerlog.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solar.crawlerlog.domain.model.LogAlreadyFinishedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LogAlreadyFinishedExceptionAdvice {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(LogAlreadyFinishedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleException(LogAlreadyFinishedException exception) {

        logger.error("Caught exception", exception);
    }
}
