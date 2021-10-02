package Xeva.productiveApp.delegatedTaskSSE;

import Xeva.productiveApp.appUser.AppUserRepository;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.delegatedTaskSSE.dto.CollaboratorEventDto;
import Xeva.productiveApp.delegatedTaskSSE.dto.EventDto;
import Xeva.productiveApp.delegatedTaskSSE.dto.IsNewTaskResponse;
import Xeva.productiveApp.delegatedTaskSSE.dto.PermissionDto;
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
    private final AppUserRepository appUserRepository;

    @Override
    public IsNewTaskResponse checkIfNewPermission(String email){
        if(this.appUserRepository.findByEmail(email).isPresent()){
            ApplicationUser user = this.appUserRepository.findByEmail(email).get();

            if(user.isNewPermission()){
                user.setNewPermission(false);
                this.appUserRepository.save(user);

                return new IsNewTaskResponse("true");
            }else{
                return new IsNewTaskResponse("false");
            }
        }else{
            return new IsNewTaskResponse("false");
        }
    }

    @Override
    public IsNewTaskResponse checkIfNewTask(String email) {
        if(this.appUserRepository.findByEmail(email).isPresent()){
            ApplicationUser user = this.appUserRepository.findByEmail(email).get();

            if(user.isNewTask()){
                user.setNewTask(false);
                this.appUserRepository.save(user);

                return new IsNewTaskResponse("true");
            }else{
                return new IsNewTaskResponse("false");
            }
        }else{
            return new IsNewTaskResponse("false");
        }
    }

    @Override
    public IsNewTaskResponse checkIfNewCollaborator(String email) {
        if(this.appUserRepository.findByEmail(email).isPresent()){
            ApplicationUser user = this.appUserRepository.findByEmail(email).get();

            if(user.isNewCollaborator()){
                user.setNewCollaborator(false);
                this.appUserRepository.save(user);

                return new IsNewTaskResponse("true");
            }else{
                return new IsNewTaskResponse("false");
            }
        }else{
            return new IsNewTaskResponse("false");
        }
    }

    @Override
    public void sendNotification(String memberId, EventDto event) {
        if(this.appUserRepository.findByEmail(memberId).isPresent()){
            ApplicationUser user = this.appUserRepository.findByEmail(memberId).get();
            user.setNewTask(true);
            this.appUserRepository.save(user);
        }

        if (event != null) {
            this.doSendNotification(memberId, event);
        }
    }

    @Override
    public void sendNotification(String memberId, CollaboratorEventDto event){
        if(event != null){
            this.doSendNotification(memberId + "collaborator", event);
        }
    }

    @Override
    public void sendNotification(String memberId, PermissionDto event) {
        if(event != null){
            this.doSendNotification(memberId + "permission", event);
        }
    }

    private void doSendNotification(String memberId, PermissionDto event) {
        emitterRepository.get(memberId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(eventMapper.toSseEventBuilder(event));
            } catch (IOException | IllegalStateException e) {
                emitterRepository.remove(memberId);
            }
        }, () -> {});
    }

    private void doSendNotification(String memberId, CollaboratorEventDto event) {
        emitterRepository.get(memberId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(eventMapper.toSseEventBuilder(event));
            } catch (IOException | IllegalStateException e) {
                emitterRepository.remove(memberId);
            }
        }, () -> {});
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
