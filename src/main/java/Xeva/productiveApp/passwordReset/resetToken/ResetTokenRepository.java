package Xeva.productiveApp.passwordReset.resetToken;

import Xeva.productiveApp.appUser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    Optional<ResetToken> findByToken(String token);

    ResetToken findByTokenAndAppUser(String token, ApplicationUser user);

    @Transactional
    void deleteAllByAppUser(ApplicationUser appUser);

    @Transactional
    @Modifying
    void deleteByAppUser(ApplicationUser appUser);

}
