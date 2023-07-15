package com.webhook.webhookscheduler.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class RestErrorController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception e) {
        log.error("Rest Service error : {}", e.getMessage() + e.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> rest404() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
    }
}
