package Xeva.productiveApp.delegatedTaskSSE;

import Xeva.productiveApp.delegatedTaskSSE.dto.CollaboratorEventDto;
import Xeva.productiveApp.delegatedTaskSSE.dto.EventDto;
import Xeva.productiveApp.delegatedTaskSSE.dto.IsNewTaskResponse;
import Xeva.productiveApp.delegatedTaskSSE.dto.PermissionDto;

public interface NotificationService {
    IsNewTaskResponse checkIfNewTask(String email);
    IsNewTaskResponse checkIfNewCollaborator(String email);
    IsNewTaskResponse checkIfNewPermission(String email);

    void sendNotification(String memberId, EventDto event);
    void sendNotification(String memberId, CollaboratorEventDto event);
    void sendNotification(String memberId, PermissionDto event);
}
