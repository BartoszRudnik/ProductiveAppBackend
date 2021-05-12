package Xeva.productiveApp.appUser.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUserRequest {

    String firstName;
    String lastName;

}
