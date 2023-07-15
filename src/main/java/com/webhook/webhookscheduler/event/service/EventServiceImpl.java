package com.webhook.webhookscheduler.event.service;

import com.webhook.webhookscheduler.event.dto.EventDto;
import com.webhook.webhookscheduler.event.dto.RepeatEventDto;
import com.webhook.webhookscheduler.event.job.EventScheduler;
import com.webhook.webhookscheduler.event.mapper.EventMapper;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class EventServiceImpl implements EventService {
    EventMapper eventMapper;
    Random random;

    private static EventServiceImpl instance;

    public static EventServiceImpl getInstance() {
        return instance;
    }

    @Autowired
    EventServiceImpl(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
        this.random = new Random(System.currentTimeMillis());
        instance = this;
    }

    @Override
    public boolean isHoliday() {
        return eventMapper.isHoliday();
    }

    @Override
    public void dropOutDatedEvent() {
        eventMapper.dropOutDatedEvent();
    }

    @Override
    public List<EventDto> eventList() {
        return eventMapper.eventList();
    }

    @Override
    public List<RepeatEventDto> repeatList() {
        return eventMapper.repeatList();
    }

    @Transactional
    @Override
    public boolean insertEvent(EventDto event) {
        int id;
        do {
            id = random.nextInt(100_000_000);
        } while (!eventMapper.checkEventIdAvailable(id));
        event.setId(id);

        boolean rst = eventMapper.insertEvent(event);
        if (rst) {
            try {
                EventScheduler.getInstance().addEvent(event);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }

        return rst;
    }

    @Transactional
    @Override
    public boolean insertRepeatEvent(RepeatEventDto event) {
        int id;
        do {
            id = random.nextInt(100_000_000);
        } while (!eventMapper.checkRepeatEventIdAvailable(id));
        event.setId(id);

        if (event.getDisableOnHoliday() == null) {
            event.setDisableOnHoliday(true);
        }

        boolean rst = eventMapper.insertRepeatEvent(event);

        if (rst) {
            try {
                EventScheduler.getInstance().repeatEvent(event);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }

        return rst;
    }

    @Transactional
    @Override
    public boolean deleteEvent(int id) {
        boolean rst = eventMapper.deleteEvent(id);

        if (rst) {
            try {
                EventScheduler.getInstance().removeJob(id, "event");
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }

        return rst;
    }

    @Override
    public boolean deleteRepeatEvent(int id) {
        boolean rst = eventMapper.deleteRepeatEvent(id);

        if (rst) {
            try {
                EventScheduler.getInstance().removeJob(id, "repeat");
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }

        return rst;
    }
}
