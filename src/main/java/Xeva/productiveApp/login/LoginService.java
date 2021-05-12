package Xeva.productiveApp.login;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.login.dto.LoginRequest;
import Xeva.productiveApp.registration.confirmationToken.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final AppUserService appUserService;

    public ConfirmationToken signIn(LoginRequest request){

        return appUserService.signInUser(request.getEmail(), request.getPassword());

    }

}
