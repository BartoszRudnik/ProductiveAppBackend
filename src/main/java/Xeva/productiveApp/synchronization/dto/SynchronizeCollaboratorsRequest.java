package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeCollaboratorsRequest {

    private Long id;
    private String email;
    private String relationState;
    private String collaboratorName;
    private boolean alreadyAsked;
    private boolean isAskingForPermission;
    private boolean sentPermission;
    private boolean receivedPermission;
    private boolean received;
    private LocalDateTime lastUpdated;
    private String uuid;

}
