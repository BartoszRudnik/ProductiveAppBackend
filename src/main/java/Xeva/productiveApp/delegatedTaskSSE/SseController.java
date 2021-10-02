package Xeva.productiveApp.delegatedTaskSSE;

import Xeva.productiveApp.delegatedTaskSSE.dto.CollaboratorEventDto;
import Xeva.productiveApp.delegatedTaskSSE.dto.EventDto;
import Xeva.productiveApp.delegatedTaskSSE.dto.IsNewTaskResponse;
import Xeva.productiveApp.delegatedTaskSSE.dto.PermissionDto;
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

    @GetMapping("/subscribePermission/{memberId}")
    public SseEmitter subscribePermissionEvents(@PathVariable String memberId){
        return this.emitterService.createPermissionEmitter(memberId);
    }

    @PostMapping("/publishPermission/{memberId}")
    public void publishPermissionEvent(@PathVariable String memberId, @RequestBody PermissionDto event){
       this.notificationService.sendNotification(memberId, event);
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

    @GetMapping("/isNewPermission/{email}")
    public IsNewTaskResponse checkIfNewPermission(@PathVariable String email){
        return this.notificationService.checkIfNewPermission(email);
    }
}
