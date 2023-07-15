package com.webhook.webhookscheduler.event.job;

import com.webhook.webhookscheduler.event.dto.EventDto;
import com.webhook.webhookscheduler.event.dto.RepeatEventDto;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventScheduler {
    Scheduler scheduler;
    private static EventScheduler instance;
    public static EventScheduler getInstance() {
        return instance;
    }

    EventScheduler() throws SchedulerException {
        SchedulerFactory factory = new StdSchedulerFactory();
        scheduler = factory.getScheduler();

        ListenerManager listenerManager = scheduler.getListenerManager();
        listenerManager.addTriggerListener(new EventTriggerListener());
        scheduler.start();

        instance = this;
    }

    public void addEvent(EventDto event) throws SchedulerException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("event", event);

        JobDetail job = JobBuilder.newJob(EventJob.class)
                .setJobData(dataMap)
                .withIdentity(String.valueOf(event.getId()), "event job")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(String.valueOf(event.getId()), "event trigger")
                .startAt(event.getDate())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .withMisfireHandlingInstructionFireNow()
                        .withRepeatCount(0))
                .build();

        scheduler.scheduleJob(job, trigger);
    }

    public void repeatEvent(RepeatEventDto event) throws SchedulerException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("repeat", event);

        JobDetail job = JobBuilder.newJob(RepeatJob.class)
                .setJobData(dataMap)
                .withIdentity(String.valueOf(event.getId()), "repeat job")
                .build();

        String cron;
        if (event.getCron() == null || event.getCron() == 0) {
            cron = String.format("0 %d %d ? * MON-FRI", event.getMinute(), event.getHour());
        } else {
            int val = event.getCron();
            StringBuilder cronBuilder = new StringBuilder();
            for (int i = 0; i < 7; ++i) {
                if (((val & (1 << i)) != 0)) {
                    cronBuilder.append(i + 1).append(",");
                }
            }
            cronBuilder.setLength(cronBuilder.length() - 1);
            cron = String.format("0 %d %d ? * %s", event.getMinute(), event.getHour(), cronBuilder);
        }

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(String.valueOf(event.getId()), "repeat trigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();

        scheduler.scheduleJob(job, trigger);
    }

    public void removeJob(int id, String group) throws SchedulerException {
        scheduler.deleteJob(JobKey.jobKey(String.valueOf(id), group));
    }

    public void removeJob(JobKey key) throws Exception {
        scheduler.deleteJob(key);
    }
}
