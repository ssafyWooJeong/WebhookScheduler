package com.webhook.webhookscheduler.event.job;

import com.webhook.webhookscheduler.event.dto.RepeatEventDto;
import com.webhook.webhookscheduler.event.service.EventPushServiceImpl;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RepeatJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        RepeatEventDto event = (RepeatEventDto) dataMap.get("repeat");
        EventPushServiceImpl.getInstance().sendRepeatedEvent(event);
    }
}
