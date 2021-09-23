package Xeva.productiveApp.task.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddResponse {
    Long taskId;
    String childTaskUuid;
}
