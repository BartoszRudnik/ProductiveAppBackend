package Xeva.productiveApp.filterSettings;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.filterSettings.pojo.CollaboratorEmailRequest;
import Xeva.productiveApp.filterSettings.pojo.FilterSettingsResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FilterSettingsService {

    private final AppUserService appUserService;
    private final FilterSettingsRepository filterSettingsRepository;

    private ApplicationUser validateRequest(String mail) {
        boolean isValidUser = appUserService.findByEmail(mail).isPresent();

        if(!isValidUser){
            throw new IllegalStateException("User not found");
        }

        ApplicationUser applicationUser = appUserService.findByEmail(mail).get();

        boolean validSettings = filterSettingsRepository.findByUser(applicationUser).isPresent();

        if(!validSettings){
            FilterSettings newSettings = new FilterSettings(applicationUser, false, false);
            filterSettingsRepository.save(newSettings);
        }

        return applicationUser;
    }

    public void changeShowOnlyUnfinished(String mail){

        ApplicationUser applicationUser = validateRequest(mail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        userSettings.setShowOnlyUnfinished(!userSettings.isShowOnlyUnfinished());

        filterSettingsRepository.save(userSettings);

    }

    public void changeShowOnlyDelegated(String mail){

        ApplicationUser applicationUser = validateRequest(mail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        userSettings.setShowOnlyDelegated(!userSettings.isShowOnlyDelegated());

        filterSettingsRepository.save(userSettings);

    }

    public void filterCollaboratorEmail(String userMail, CollaboratorEmailRequest collaboratorMail){

        ApplicationUser applicationUser = validateRequest(userMail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        userSettings.setCollaboratorEmail(collaboratorMail.getCollaboratorEmail());

        filterSettingsRepository.save(userSettings);

    }

    public FilterSettingsResponse getFilterSettings(String mail) {

        ApplicationUser applicationUser = validateRequest(mail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        return new FilterSettingsResponse(userSettings.isShowOnlyUnfinished(), userSettings.isShowOnlyDelegated(), userSettings.getCollaboratorEmail());

    }

}
