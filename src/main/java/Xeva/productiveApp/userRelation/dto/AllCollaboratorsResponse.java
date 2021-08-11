package Xeva.productiveApp.userRelation.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AllCollaboratorsResponse {

    Long id;
    String invitationSender;
    String invitationReceiver;
    String invitationSenderName;
    String invitationReceiverName;
    String relationState;
    boolean user1Permission;
    boolean user2Permission;
    private boolean user1AskForPermission;
    private boolean user2AskForPermission;

}
