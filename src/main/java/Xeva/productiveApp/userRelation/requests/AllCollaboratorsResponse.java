package Xeva.productiveApp.userRelation.requests;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AllCollaboratorsResponse {

    String collaboratorEmail;
    String relationState;

}
