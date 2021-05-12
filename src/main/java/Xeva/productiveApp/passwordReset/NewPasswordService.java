package Xeva.productiveApp.passwordReset;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.passwordReset.dto.NewPasswordRequest;
import Xeva.productiveApp.passwordReset.resetToken.ResetToken;
import Xeva.productiveApp.passwordReset.resetToken.ResetTokenService;
import Xeva.productiveApp.registration.confirmationToken.ConfirmationToken;
import Xeva.productiveApp.registration.confirmationToken.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NewPasswordService {

    private final AppUserService appUserService;
    private final ResetTokenService resetTokenService;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ConfirmationToken newPassword(NewPasswordRequest request){

        boolean userExists = appUserService.findByEmail(request.getEmail()).isPresent();
        boolean tokenExists = resetTokenService.getToken(request.getToken()).isPresent();

        if(!userExists){
            throw new IllegalStateException("User with that email don't exists");
        }

        if(!tokenExists){
            throw new IllegalStateException("Wrong token");
        }

        ApplicationUser appUser = appUserService.findByEmail(request.getEmail()).get();
        ResetToken databaseToken = resetTokenService.getToken(request.getToken()).get();

        if(appUser.getId() != databaseToken.getAppUser().getId()){
            throw new IllegalStateException("Token don't match with user email");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(request.getNewPassword());

        appUserService.updateUserPassword(request.getEmail(), encodedPassword);
        appUserService.enableAppUser(request.getEmail());

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.updateConfirmationToken(confirmationToken);
        resetTokenService.deleteToken(appUser);

        return confirmationToken;

    }

}
