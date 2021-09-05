package Xeva.productiveApp.synchronization.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeleteTask {

    private Long taskId;
    private String ownerEmail;

}
