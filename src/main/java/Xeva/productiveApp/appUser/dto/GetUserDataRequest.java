package Xeva.productiveApp.appUser.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetUserDataRequest {

    String firstName;
    String lastName;

}
