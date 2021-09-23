package Xeva.productiveApp.delegatedTaskSSE;

import Xeva.productiveApp.delegatedTaskSSE.dto.EventDto;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Primary
@AllArgsConstructor
public class SseNotificationService implements NotificationService{
    private final EmitterRepository emitterRepository;
    private final EventMapper eventMapper;

    @Override
    public void sendNotification(String memberId, EventDto event) {
        if (event != null) {
            this.doSendNotification(memberId, event);
        }
    }

    private void doSendNotification(String memberId, EventDto event) {
        emitterRepository.get(memberId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(eventMapper.toSseEventBuilder(event));
            } catch (IOException | IllegalStateException e) {
                emitterRepository.remove(memberId);
            }
        }, () -> {});
    }
}
