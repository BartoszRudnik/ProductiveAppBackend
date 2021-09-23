package Xeva.productiveApp.delegatedTaskSSE;

import Xeva.productiveApp.delegatedTaskSSE.dto.EventDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(path = "api/v1/delegatedTaskSSE")
@AllArgsConstructor
public class SseController {
    private final EmitterService  emitterService;
    private final NotificationService notificationService;

    @GetMapping("/subscribe/{memberId}")
    public SseEmitter subscribeToEvents(@PathVariable String memberId) {
        return this.emitterService.createEmitter(memberId);
    }

    @PostMapping("/publish/{memberId}")
    public void publishEvent(@PathVariable String memberId, @RequestBody EventDto event) {
        this.notificationService.sendNotification(memberId, event);
    }
}
