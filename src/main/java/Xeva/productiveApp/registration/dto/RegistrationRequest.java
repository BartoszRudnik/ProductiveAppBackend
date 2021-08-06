package Xeva.productiveApp.registration.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String userType;

}
