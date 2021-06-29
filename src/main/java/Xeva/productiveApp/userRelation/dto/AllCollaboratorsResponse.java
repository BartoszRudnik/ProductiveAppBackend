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
    String relationState;
    boolean user1Permission;
    boolean user2Permission;

}
