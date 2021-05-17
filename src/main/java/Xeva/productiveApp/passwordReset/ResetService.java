package Xeva.productiveApp.passwordReset;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.email.EmailSender;
import Xeva.productiveApp.passwordReset.dto.ResetRequest;
import Xeva.productiveApp.passwordReset.resetToken.ResetToken;
import Xeva.productiveApp.passwordReset.resetToken.ResetTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ResetService {

    private final AppUserService appUserService;
    private final ResetTokenService resetTokenService;
    private final EmailSender emailSender;

    private String generateResetToken(){

        int tokenLen = 6;

        String digits = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(tokenLen);

        for(int i = 0; i < tokenLen; i++) {
            stringBuilder.append(digits.charAt(random.nextInt(digits.length())));
        }

        return stringBuilder.toString();

    }

    public void resetPassword(ResetRequest request){

        boolean userExists = appUserService
                .findByEmail(request.getEmail())
                .isPresent();

        if(!userExists){
            throw new IllegalStateException("Wrong email or password");
        }

        appUserService.disableAppUser(request.getEmail());
        Optional<ApplicationUser> user = appUserService.findByEmail(request.getEmail());

        String code = generateResetToken();

        ResetToken resetToken = new ResetToken(
                code, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user.get());

        resetTokenService.saveResetToken(resetToken);

        emailSender.send("Reset password",request.getEmail(), emailSender.buildEmailPasswordReset(user.get().getFirstName(), code));

    }

}
