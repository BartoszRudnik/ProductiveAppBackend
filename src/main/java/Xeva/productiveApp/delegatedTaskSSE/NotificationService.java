package Xeva.productiveApp.delegatedTaskSSE;

import Xeva.productiveApp.delegatedTaskSSE.dto.EventDto;

public interface NotificationService {

    void sendNotification(String memberId, EventDto event);
}
