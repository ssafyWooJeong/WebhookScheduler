package com.webhook.webhookscheduler.event;

import com.webhook.webhookscheduler.event.mapper.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class EventTest {

    @Autowired
    EventMapper eventMapper;

    @Test
    @Transactional
    public void checkHoliday() {
        //assertFalse(eventMapper.isHoliday());
    }

    @Test
    @Transactional
    public void dropOutDatedEvent() {
        //eventMapper.dropOutDatedEvent();
    }

    @Test
    @Transactional
    public void listEvent() {
        //List<RepeatEventDto> list = eventMapper.repeatList();
        //assertEquals(2, list.size());
        //log.debug("list : {}", Arrays.toString(list.toArray()));
    }

    @Test
    @Transactional
    public void insertTest() {
        //Calendar calendar = Calendar.getInstance();
        //calendar.set(2023, 7,1);
        //RepeatEventDto event = RepeatEventDto.builder().id(new Random().nextInt(100000000))
        //        .until(calendar.getTime()).hour(9).minute(0).message("테스트").url("11").disableOnHoliday(true).build();

        //eventMapper.insertRepeatEvent(event);
    }
}
