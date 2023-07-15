package com.webhook.webhookscheduler.event.service;

import com.webhook.webhookscheduler.event.dto.EventDto;
import com.webhook.webhookscheduler.event.dto.RepeatEventDto;

public interface EventPushService {
    boolean sendEvent(EventDto eventDto);

    boolean sendRepeatedEvent(RepeatEventDto eventDto);
}
