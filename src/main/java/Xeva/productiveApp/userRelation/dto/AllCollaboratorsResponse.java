package Xeva.productiveApp.userRelation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AllCollaboratorsResponse {

    private Long id;
    private String invitationSender;
    private String invitationReceiver;
    private String invitationSenderName;
    private String invitationReceiverName;
    private String relationState;
    private boolean user1Permission;
    private boolean user2Permission;
    private boolean user1AskForPermission;
    private boolean user2AskForPermission;
    private LocalDateTime lastUpdated;

}
