package com.webhook.webhookscheduler.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "redirect:/index.html";
    }

    @GetMapping("/error")
    public String error() {
        return "redirect:/index.html";
    }
}
