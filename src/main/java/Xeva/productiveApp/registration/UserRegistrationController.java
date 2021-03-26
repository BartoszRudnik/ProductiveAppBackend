package Xeva.productiveApp.registration;

import Xeva.productiveApp.registration.token.ConfirmationToken;
import Xeva.productiveApp.registration.token.ResponseToken;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class UserRegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseToken register(@RequestBody RegistrationRequest request){

        ConfirmationToken token = registrationService.register(request);

        return new ResponseToken(token.getToken(), Duration.between(token.getCreatedAt(), token.getExpiresAt()).toMillis());

    };

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }

}
