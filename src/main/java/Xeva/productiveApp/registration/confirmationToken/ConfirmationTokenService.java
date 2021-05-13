package Xeva.productiveApp.registration.confirmationToken;

import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public int updateConfirmationToken(ConfirmationToken token){
        return confirmationTokenRepository.updateToken(token.getToken(), token.getCreatedAt(), token.getExpiresAt(), token.getAppUser().getId());
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public void deleteConfirmationToken(ApplicationUser applicationUser){
        confirmationTokenRepository.deleteByAppUser(applicationUser);
    }

}
