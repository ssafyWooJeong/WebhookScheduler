package com.webhook.webhookscheduler.event.job;

import com.webhook.webhookscheduler.event.dto.EventDto;
import com.webhook.webhookscheduler.event.service.EventPushServiceImpl;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class EventJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        EventDto event = (EventDto) dataMap.get("event");
        EventPushServiceImpl.getInstance().sendEvent(event);
    }
}
