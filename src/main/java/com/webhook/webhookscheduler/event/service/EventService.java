package com.webhook.webhookscheduler.event.service;

import com.webhook.webhookscheduler.event.dto.EventDto;
import com.webhook.webhookscheduler.event.dto.RepeatEventDto;

import java.util.List;

public interface EventService {
    boolean isHoliday();

    void dropOutDatedEvent();

    List<EventDto> eventList();

    List<RepeatEventDto> repeatList();

    boolean insertEvent(EventDto event);

    boolean insertRepeatEvent(RepeatEventDto event);

    boolean deleteEvent(int id);

    boolean deleteRepeatEvent(int id);
}
