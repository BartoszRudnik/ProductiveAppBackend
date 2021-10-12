package Xeva.productiveApp.registration;

import Xeva.productiveApp.appUser.AppUserRole;
import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.email.EmailSender;
import Xeva.productiveApp.registration.confirmationToken.ConfirmationToken;
import Xeva.productiveApp.registration.confirmationToken.ConfirmationTokenService;
import Xeva.productiveApp.registration.dto.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public ConfirmationToken register(RegistrationRequest request){

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if(!isValidEmail){
            throw new IllegalStateException("email not valid");
        }

        ConfirmationToken confirmationToken = this.appUserService.signUpUser(new ApplicationUser(
                request.getFirstName(), request.getLastName(), request.getEmail().toLowerCase(), request.getPassword(), AppUserRole.MAIL_USER));

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + confirmationToken.getToken();

        emailSender.send("Confirm your registration", request.getEmail(), emailSender.buildEmailRegistration(request.getFirstName(), link));

        return confirmationToken;

    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }

}
