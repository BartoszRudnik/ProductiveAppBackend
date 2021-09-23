package Xeva.productiveApp.delegatedTaskSSE;

import Xeva.productiveApp.delegatedTaskSSE.dto.EventDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import wiremock.org.apache.commons.lang3.RandomStringUtils;

@Component
@AllArgsConstructor
public class EventMapper {
    public SseEmitter.SseEventBuilder toSseEventBuilder(EventDto event) {
        return SseEmitter.event()
                .id(RandomStringUtils.randomAlphanumeric(12))
                .name(event.getTaskUuid())
                .data(event.getUserMail());


    }
}
