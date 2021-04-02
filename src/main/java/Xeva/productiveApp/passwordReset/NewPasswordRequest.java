package Xeva.productiveApp.passwordReset;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewPasswordRequest {

    private String email;
    private String token;
    private String newPassword;

}
