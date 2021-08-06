package Xeva.productiveApp.login;

import Xeva.productiveApp.login.dto.LoginRequest;
import Xeva.productiveApp.registration.confirmationToken.ConfirmationToken;
import Xeva.productiveApp.registration.confirmationToken.ResponseToken;
import Xeva.productiveApp.registration.dto.RegistrationRequest;
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

    @PostMapping("/googleLogin")
    public ResponseToken googleLogin(@RequestBody RegistrationRequest request){
        ConfirmationToken token = loginService.googleSignIn(request);

        return new ResponseToken(token.getToken(), Duration.between(token.getCreatedAt(), token.getExpiresAt()).toMillis());

    }

}
