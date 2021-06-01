package Xeva.productiveApp.localization;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.localization.dto.AddLocalization;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LocalizationService {

    private final AppUserService appUserService;
    private final LocalizationRepository localizationRepository;

    public Localization getLocalization(Long id){

        if(this.localizationRepository.findById(id).isPresent()) {

            return this.localizationRepository.findById(id).get();

        }else{

            return null;

        }

    }

    public List<Localization> getLocalizations(String mail){

        boolean isValidUser = appUserService.findByEmail(mail).isPresent();

        if(!isValidUser){
            throw new IllegalStateException("Wrong user");
        }

        ApplicationUser user = appUserService.findByEmail(mail).get();

        return this.localizationRepository.findAllByUser(user);

    }

    public Long addLocalization(String mail, AddLocalization addLocalization){

        boolean isValidUser = appUserService.findByEmail(mail).isPresent();

        if(!isValidUser){
            throw new IllegalStateException("Wrong user");
        }

        ApplicationUser user = appUserService.findByEmail(mail).get();

        Localization localization = new Localization(addLocalization.getLocalizationName(), addLocalization.getLongitude(), addLocalization.getLatitude(), user);

        this.localizationRepository.save(localization);

        return localization.getLocalizationId();

    }

    public void deleteLocalization(Long id){

        this.localizationRepository.deleteById(id);

    }

    public void updateLocalization(Long id, AddLocalization addLocalization){

        boolean isValidLocalization = this.localizationRepository.findById(id).isPresent();

        if(!isValidLocalization){
            throw new IllegalStateException("Localization doesn't exists");
        }

        Localization localizationToEdit = this.localizationRepository.findById(id).get();

        localizationToEdit.setLocalizationName(addLocalization.getLocalizationName());
        localizationToEdit.setLatitude(addLocalization.getLatitude());
        localizationToEdit.setLongitude(addLocalization.getLongitude());

        this.localizationRepository.save(localizationToEdit);

    }

    public Optional<Localization> findById(Long id){
        if(id == null){
            return Optional.empty();
        }
        return this.localizationRepository.findById(id);
    }

    public void deleteAllUserLocalizations(ApplicationUser user){

        this.localizationRepository.deleteAllByUser(user);

    }

}
