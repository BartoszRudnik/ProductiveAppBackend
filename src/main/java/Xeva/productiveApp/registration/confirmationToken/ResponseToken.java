package Xeva.productiveApp.registration.confirmationToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ResponseToken {

    private String token;
    private Long tokenDuration;

}
