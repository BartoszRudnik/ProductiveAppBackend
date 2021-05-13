package Xeva.productiveApp.passwordReset.resetToken;

import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ResetTokenService {

    private final ResetTokenRepository resetTokenRepository;

    public void saveResetToken(ResetToken token){
        resetTokenRepository.save(token);
    }

    public void deleteToken(ApplicationUser appUser){
        resetTokenRepository.deleteByAppUser(appUser);
    }

    public Optional<ResetToken> getToken(String token){
        return resetTokenRepository.findByToken(token);
    }

    public ResetToken getTokenByTokenAndUser(ApplicationUser user, String token){
        return resetTokenRepository.findByTokenAndAppUser(token, user);
    }

    public void deleteUserTokens(ApplicationUser user){
        resetTokenRepository.deleteAllByAppUser(user);
    }

}
