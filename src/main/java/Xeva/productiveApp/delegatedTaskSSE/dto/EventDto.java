package Xeva.productiveApp.delegatedTaskSSE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto implements Serializable {
    private String userMail;
    private String taskUuid;
}
