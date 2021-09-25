package Xeva.productiveApp.delegatedTaskSSE;

import Xeva.productiveApp.delegatedTaskSSE.dto.CollaboratorEventDto;
import Xeva.productiveApp.delegatedTaskSSE.dto.EventDto;
import Xeva.productiveApp.delegatedTaskSSE.dto.IsNewTaskResponse;

public interface NotificationService {
    IsNewTaskResponse checkIfNewTask(String email);
    IsNewTaskResponse checkIfNewCollaborator(String email);

    void sendNotification(String memberId, EventDto event);
    void sendNotification(String memberId, CollaboratorEventDto event);
}
