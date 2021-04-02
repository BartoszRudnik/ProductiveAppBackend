package Xeva.productiveApp.passwordReset.resetToken;

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

    public Optional<ResetToken> getToken(String token){
        return resetTokenRepository.findByToken(token);
    }

}