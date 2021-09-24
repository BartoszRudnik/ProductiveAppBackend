package Xeva.productiveApp.delegatedTaskSSE;

import Xeva.productiveApp.delegatedTaskSSE.dto.EventDto;
import Xeva.productiveApp.delegatedTaskSSE.dto.IsNewTaskResponse;

public interface NotificationService {

    IsNewTaskResponse checkIfNewTask(String email);

    void sendNotification(String memberId, EventDto event);
}
