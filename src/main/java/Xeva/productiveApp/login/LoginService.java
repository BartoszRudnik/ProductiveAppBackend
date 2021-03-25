package Xeva.productiveApp.login;

import Xeva.productiveApp.appUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final AppUserService appUserService;

    public String signIn(LoginRequest request){

        return appUserService.signInUser(request.getEmail(), request.getPassword());

    }

}
