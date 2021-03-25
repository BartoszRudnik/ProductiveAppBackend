package Xeva.productiveApp.registration;

import Xeva.productiveApp.appUser.AppUserRole;
import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    public String register(RegistrationRequest request){

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if(!isValidEmail){
            throw new IllegalStateException("email not valid");
        }

        return appUserService.signUpUser(new ApplicationUser(
                request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), AppUserRole.USER));

    }

}
