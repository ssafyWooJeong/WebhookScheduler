package com.webhook.webhookscheduler;

import com.webhook.webhookscheduler.event.dto.EventDto;
import com.webhook.webhookscheduler.event.dto.RepeatEventDto;
import com.webhook.webhookscheduler.event.job.EventScheduler;
import com.webhook.webhookscheduler.event.service.EventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "com.webhook")
@MapperScan(basePackages = "com.webhook.**.mapper")
public class WebhookSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhookSchedulerApplication.class, args);

        EventServiceImpl.getInstance().dropOutDatedEvent();
        List<EventDto> eventList = EventServiceImpl.getInstance().eventList();
        List<RepeatEventDto> repeastList = EventServiceImpl.getInstance().repeatList();

        EventScheduler scheduler = EventScheduler.getInstance();

        try {
            for (EventDto event : eventList) {
                scheduler.addEvent(event);
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }

        try {
            for (RepeatEventDto eventDto : repeastList) {
                scheduler.repeatEvent(eventDto);
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }

        log.info("application started");
    }
}
