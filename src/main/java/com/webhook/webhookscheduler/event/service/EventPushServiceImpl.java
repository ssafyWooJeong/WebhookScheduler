package com.webhook.webhookscheduler.event.service;

import com.webhook.webhookscheduler.event.dto.EventDto;
import com.webhook.webhookscheduler.event.dto.RepeatEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EventPushServiceImpl implements EventPushService {
    EventService eventService;
    private static EventPushServiceImpl instance;

    @Autowired
    EventPushServiceImpl(EventService eventService) {
        this.eventService = eventService;
        instance = this;
    }

    public static EventPushServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean sendEvent(EventDto event) {
        Map<String, String> body = new HashMap<>();
        body.put("text", event.getMessage());
        return sendMessage(event.getUrl(), body);
    }

    @Override
    public boolean sendRepeatedEvent(RepeatEventDto event) {
        if (event.getDisableOnHoliday() && eventService.isHoliday())
            return true;
        Map<String, String> body = new HashMap<>();
        body.put("text", event.getMessage());

        return sendMessage(event.getUrl(), body);
    }

    private static boolean sendMessage(String url, Map<String, String> body) {
        WebClient client = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        String res = client.post().uri(url).body(BodyInserters.fromValue(body)).retrieve().bodyToMono(String.class).block();
        if (!"ok".equals(res)) {
            log.error("sending event to {} failed", url);
            return false;
        }

        return true;
    }
}
