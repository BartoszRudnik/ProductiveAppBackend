package Xeva.productiveApp.passwordReset;

import Xeva.productiveApp.registration.confirmationToken.ConfirmationToken;
import Xeva.productiveApp.registration.confirmationToken.ResponseToken;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/newPassword")
@AllArgsConstructor
public class NewPasswordController {

    private final NewPasswordService newPasswordService;

    @PostMapping
    public ResponseToken newPassword(@RequestBody NewPasswordRequest request){

        ConfirmationToken token = newPasswordService.newPassword(request);

        return new ResponseToken(token.getToken(), Duration.between(token.getCreatedAt(), token.getExpiresAt()).toMillis());

    }

}
