package com.webhook.webhookscheduler.event.controller;

import com.webhook.webhookscheduler.event.dto.EventDto;
import com.webhook.webhookscheduler.event.dto.RepeatEventDto;
import com.webhook.webhookscheduler.event.job.EventScheduler;
import com.webhook.webhookscheduler.event.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/event")
public class EventRestController {
    EventService eventService;

    @Autowired
    public EventRestController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/once")
    public ResponseEntity<Integer> insertEvent(@RequestBody EventDto event) {
        boolean rst = eventService.insertEvent(event);
        if (rst) {
            return new ResponseEntity<>(event.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/once/{id}")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable int id) throws SchedulerException {
        boolean rst = eventService.deleteEvent(id);
        if (rst) {
            EventScheduler.getInstance().removeJob(id, "event");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/repeat")
    public ResponseEntity<Integer> insertRepeatEvent(@RequestBody RepeatEventDto event) {
        boolean rst = eventService.insertRepeatEvent(event);
        if (rst) {
            return new ResponseEntity<>(event.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/repeat/{id}")
    public ResponseEntity<Boolean> deleteRepeatEvent(@PathVariable int id) {
        boolean rst = eventService.deleteRepeatEvent(id);
        if (rst) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
