package Xeva.productiveApp.registration;

import Xeva.productiveApp.registration.confirmationToken.ConfirmationToken;
import Xeva.productiveApp.registration.confirmationToken.ResponseToken;
import Xeva.productiveApp.registration.dto.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public ResponseToken register(@RequestBody RegistrationRequest request){
        ConfirmationToken token = this.registrationService.register(request);

        return new ResponseToken(token.getToken(), Duration.between(token.getCreatedAt(), token.getExpiresAt()).toMillis(), token.getAppUser().getId(), token.getAppUser().isFirstLogin());
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token){
        return this.registrationService.confirmToken(token);
    }
}
