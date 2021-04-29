package Xeva.productiveApp.userRelation.requests;

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

}
