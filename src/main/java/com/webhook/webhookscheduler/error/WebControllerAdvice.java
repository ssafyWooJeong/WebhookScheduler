package com.webhook.webhookscheduler.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String error() {
        return "index.html";
    }
}
