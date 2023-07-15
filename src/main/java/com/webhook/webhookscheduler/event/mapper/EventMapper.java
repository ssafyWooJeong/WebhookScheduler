package com.webhook.webhookscheduler.event.mapper;

import com.webhook.webhookscheduler.event.dto.EventDto;
import com.webhook.webhookscheduler.event.dto.RepeatEventDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventMapper {
    boolean isHoliday();

    void dropOutDatedEvent();

    List<EventDto> eventList();

    List<RepeatEventDto> repeatList();

    boolean insertEvent(EventDto event);

    boolean insertRepeatEvent(RepeatEventDto event);

    boolean deleteEvent(int id);

    boolean deleteRepeatEvent(int id);

    boolean checkEventIdAvailable(int id);

    boolean checkRepeatEventIdAvailable(int id);
}
