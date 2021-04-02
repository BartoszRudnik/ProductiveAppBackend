package Xeva.productiveApp.passwordReset;

import Xeva.productiveApp.appUser.AppUserRepository;
import Xeva.productiveApp.appUser.ApplicationUser;
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

    private final AppUserRepository appUserRepository;
    private final ResetTokenService resetTokenService;

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

        boolean userExists = appUserRepository
                .findByEmail(request.getEmail())
                .isPresent();

        if(!userExists){
            throw new IllegalStateException("Wrong email or password");
        }

        Optional<ApplicationUser> user = appUserRepository.findByEmail(request.getEmail());

        ResetToken resetToken = new ResetToken(
                generateResetToken(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user.get());

        resetTokenService.saveResetToken(resetToken);

        //SEND MAIL WITH TOKEN

    }

}
