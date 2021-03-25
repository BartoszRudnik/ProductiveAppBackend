package Xeva.productiveApp.appUser;

import Xeva.productiveApp.registration.token.ConfirmationToken;
import Xeva.productiveApp.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return appUserRepository.
                findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

    }

    public String signInUser(String email, String password){

        boolean userExists = appUserRepository
                .findByEmail(email)
                .isPresent();

        if(!userExists){
            throw new IllegalStateException("Wrong email or password");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        Optional<ApplicationUser> testUser = appUserRepository.findByEmail(email);

        if(testUser.get().getPassword() != encodedPassword){
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

        return token;

    }

    public String signUpUser(ApplicationUser applicationUser){

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

        return token;

    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

}
