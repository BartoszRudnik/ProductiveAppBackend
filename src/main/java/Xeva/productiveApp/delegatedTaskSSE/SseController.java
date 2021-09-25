package Xeva.productiveApp.delegatedTaskSSE;

import Xeva.productiveApp.delegatedTaskSSE.dto.CollaboratorEventDto;
import Xeva.productiveApp.delegatedTaskSSE.dto.EventDto;
import Xeva.productiveApp.delegatedTaskSSE.dto.IsNewTaskResponse;
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

    @GetMapping("/subscribeCollaborators/{memberId}")
    public SseEmitter subscribeToCollaboratorEvents(@PathVariable String memberId){
        return this.emitterService.createCollaboratorEmitter(memberId);
    }

    @PostMapping("/publish/{memberId}")
    public void publishEvent(@PathVariable String memberId, @RequestBody EventDto event) {
        this.notificationService.sendNotification(memberId, event);
    }

    @PostMapping("/publishCollaborator/{memberId}")
    public void publishCollaboratorEvent(@PathVariable String memberId, @RequestBody CollaboratorEventDto event){
        this.notificationService.sendNotification(memberId, event);
    }

    @GetMapping("/isNewTask/{email}")
    public IsNewTaskResponse checkIfNewTask(@PathVariable String email){
        return this.notificationService.checkIfNewTask(email);
    }

    @GetMapping("/isNewCollaborator/{email}")
    public IsNewTaskResponse checkIfNewCollaborator(@PathVariable String email){
        return this.notificationService.checkIfNewCollaborator(email);
    }
}
