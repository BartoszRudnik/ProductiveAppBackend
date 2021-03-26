package Xeva.productiveApp.login;

import Xeva.productiveApp.registration.token.ConfirmationToken;
import Xeva.productiveApp.registration.token.ResponseToken;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/login")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseToken login(@RequestBody LoginRequest request){

        ConfirmationToken token = loginService.signIn(request);

        return new ResponseToken(token.getToken(), Duration.between(token.getCreatedAt(), token.getExpiresAt()).toMillis());


    }

}
