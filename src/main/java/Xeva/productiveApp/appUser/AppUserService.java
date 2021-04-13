package Xeva.productiveApp.appUser;

import Xeva.productiveApp.registration.confirmationToken.ConfirmationToken;
import Xeva.productiveApp.registration.confirmationToken.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    public Optional<ApplicationUser> findByEmail(String email){
        return appUserRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return appUserRepository.
                findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

    }

    public ConfirmationToken signInUser(String email, String password){

        boolean userExists = appUserRepository
                .findByEmail(email)
                .isPresent();

        if(!userExists){
            throw new IllegalStateException("Wrong email or password");
        }

        Optional<ApplicationUser> testUser = appUserRepository.findByEmail(email);

        if(!bCryptPasswordEncoder.matches(password, testUser.get().getPassword()) || !testUser.get().getEnabled()){
            throw new IllegalStateException("Wrong email or password");
        }

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                testUser.get()
        );

        confirmationTokenService.updateConfirmationToken(confirmationToken);

        return confirmationToken;

    }

    public ConfirmationToken signUpUser(ApplicationUser applicationUser){

        boolean userExists = appUserRepository
                .findByEmail(applicationUser.getEmail())
                .isPresent();

        if(userExists){
            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(applicationUser.getPassword());

        applicationUser.setPassword(encodedPassword);

        appUserRepository.save(applicationUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                applicationUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return confirmationToken;

    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public int disableAppUser(String email){
        return appUserRepository.disableAppUser(email);
    }

    public int updateUserPassword(String email, String newPassword){
        return appUserRepository.updateAppUserPassword(email, newPassword);
    }

}
