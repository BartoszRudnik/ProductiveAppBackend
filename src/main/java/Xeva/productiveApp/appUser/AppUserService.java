package Xeva.productiveApp.appUser;

import Xeva.productiveApp.appUser.dto.GetUserDataRequest;
import Xeva.productiveApp.appUser.dto.UpdateUserRequest;
import Xeva.productiveApp.registration.confirmationToken.ConfirmationToken;
import Xeva.productiveApp.registration.confirmationToken.ConfirmationTokenService;
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

    //Wczytanie potrzebnych interfejsów
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    //Znajdź użytkownika po emailu
    public Optional<ApplicationUser> findByEmail(String email){
        return appUserRepository.findByEmail(email);
    }

    //Usuń użytkownika
    public void deleteUser(ApplicationUser user){
        appUserRepository.delete(user);
    }

    //Wyczyść dane użytkownika
    public void clearUserData(String userMail){

        boolean isUser = findByEmail(userMail).isPresent();

        if(!isUser){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = findByEmail(userMail).get();

        user.setLastName(null);
        user.setFirstName(null);

        appUserRepository.save(user);

    }

    //Weź dane użytkownika
    public GetUserDataRequest getUserData(String userMail){

        boolean isUser = findByEmail(userMail).isPresent();

        if(!isUser){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = findByEmail(userMail).get();

        return new GetUserDataRequest(user.getFirstName(), user.getLastName());

    }

    //Zaktualizuj dane użytkownika
    public void updateUserData(String userMail, UpdateUserRequest request){

        boolean isUser = findByEmail(userMail).isPresent();

        if(!isUser){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = findByEmail(userMail).get();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        appUserRepository.save(user);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return appUserRepository.
                findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

    }

    //Metody związane z tokenami
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

    //Aktywacja/Dezaktywacja użytkownika
    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public int disableAppUser(String email){
        return appUserRepository.disableAppUser(email);
    }

    //Zaktualizuj hasło użytkownika
    public int updateUserPassword(String email, String newPassword){
        return appUserRepository.updateAppUserPassword(email, newPassword);
    }

}
