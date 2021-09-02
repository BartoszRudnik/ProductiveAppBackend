package Xeva.productiveApp.locale;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.locale.dto.LocaleRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LocaleService {

    private final LocaleRepository localeRepository;
    private final AppUserService appUserService;

    public void setLocale(LocaleRequest request, String email){
        boolean userExists = this.appUserService.findByEmail(email).isPresent();

        if(!userExists){
            throw new IllegalStateException("User doesn't exist");
        }

        ApplicationUser user = this.appUserService.findByEmail(email).get();
        Locale locale;

        if(this.localeRepository.findByLanguageCode(request.getLanguageCode()).isPresent()){
            locale = this.localeRepository.findByLanguageCode(request.getLanguageCode()).get();
        }else{
            locale = new Locale(request.getLanguageCode());
        }

        user.setLastUpdatedLocale(LocalDateTime.now());

        locale.addUser(user);

        this.localeRepository.save(locale);
    }

    public void setLocale(String languageCode, ApplicationUser user){
        Locale locale;

        if(this.localeRepository.findByLanguageCode(languageCode).isPresent()){
            locale = this.localeRepository.findByLanguageCode(languageCode).get();
        }else{
            locale = new Locale(languageCode);
        }

        user.setLastUpdatedLocale(LocalDateTime.now());

        locale.addUser(user);

        this.localeRepository.save(locale);
    }

    public LocaleRequest getLocale(String email){
        boolean userExists = this.appUserService.findByEmail(email).isPresent();

        if(!userExists){
            throw new IllegalStateException("User doesn't exist");
        }

        ApplicationUser user = this.appUserService.findByEmail(email).get();

        LocaleRequest result;

        if(user.getLocale() != null){
            result = new LocaleRequest(user.getLocale().getLanguageCode());
        }else{
            result = new LocaleRequest("en");
        }

        return result;
    }

}
