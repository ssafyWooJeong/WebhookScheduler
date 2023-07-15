package com.webhook.webhookscheduler.event.job;

import com.webhook.webhookscheduler.event.dto.EventDto;
import com.webhook.webhookscheduler.event.dto.RepeatEventDto;
import com.webhook.webhookscheduler.event.service.EventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class EventTriggerListener implements TriggerListener {

    @Override
    public String getName() {
        return EventTriggerListener.class.getName();
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        log.info("trigger {} fired. job : {}", trigger.getKey(), jobExecutionContext.getJobDetail().getKey());
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        if (dataMap.containsKey("repeat")) {
            RepeatEventDto event = (RepeatEventDto) dataMap.get("repeat");
            LocalDate localDate = new java.sql.Date(event.getUntil().getTime()).toLocalDate();

            if (localDate.isBefore(LocalDate.now())) {
                EventServiceImpl.getInstance().deleteRepeatEvent(event.getId());
                try {
                    EventScheduler.getInstance().removeJob(trigger.getJobKey());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        log.info("trigger {} misfired", trigger.getKey());
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        if (dataMap.containsKey("event")) {
            EventDto event = (EventDto) dataMap.get("event");
            EventServiceImpl.getInstance().deleteEvent(event.getId());
            try {
                EventScheduler.getInstance().removeJob(trigger.getJobKey());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        log.info("trigger {} completed", trigger.getKey());
    }
}
