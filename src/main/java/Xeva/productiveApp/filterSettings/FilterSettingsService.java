package Xeva.productiveApp.filterSettings;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.filterSettings.dto.*;
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
            FilterSettings newSettings = new FilterSettings(applicationUser, false, false, false);
            filterSettingsRepository.save(newSettings);
        }

        return applicationUser;
    }

    public void changeShowOnlyWithLocation(String mail){
        ApplicationUser applicationUser = validateRequest(mail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        userSettings.setShowOnlyWithLocation(!userSettings.isShowOnlyWithLocation());

        filterSettingsRepository.save(userSettings);

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

        System.out.println(userSettings.isShowOnlyDelegated());

        userSettings.setShowOnlyDelegated(!userSettings.isShowOnlyDelegated());

        System.out.println(userSettings.isShowOnlyDelegated());

        filterSettingsRepository.save(userSettings);

    }

    public void addFilterCollaboratorEmail(String userMail, CollaboratorEmailRequest collaboratorMail){

        ApplicationUser applicationUser = validateRequest(userMail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        for(String collaborator: collaboratorMail.getCollaboratorEmail()){

            if(!userSettings.getCollaboratorEmail().contains(collaborator)){

                userSettings.getCollaboratorEmail().add(collaborator);

            }

        }

        filterSettingsRepository.save(userSettings);

    }

    public void deleteFilterCollaboratorEmail(String userMail, DeleteCollaboratorEmailRequest collaboratorMail){

        ApplicationUser applicationUser = validateRequest(userMail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        userSettings.getCollaboratorEmail().remove(collaboratorMail.getCollaboratorEmail());

        filterSettingsRepository.save(userSettings);

    }

    public void clearFilterCollaborators(String userMail){

        ApplicationUser applicationUser = validateRequest(userMail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        userSettings.getCollaboratorEmail().clear();

        filterSettingsRepository.save(userSettings);

    }

    public void addFilterTags(String userMail, TagsRequest tags){

        ApplicationUser applicationUser = validateRequest(userMail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        for(String tag : tags.getTags()){

            if(!userSettings.getTags().contains(tag)){

                userSettings.getTags().add(tag);

            }

        }

        filterSettingsRepository.save(userSettings);

    }

    public void deleteFilterTag(String userMail, DeleteTag tag){

        ApplicationUser applicationUser = validateRequest(userMail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        userSettings.getTags().remove(tag.getTag());

        filterSettingsRepository.save(userSettings);

    }

    public void clearFilterTags(String userMail){

        ApplicationUser applicationUser = validateRequest(userMail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        userSettings.getTags().clear();

        filterSettingsRepository.save(userSettings);

    }

    public void changeSortingMode(String userMail, SortingModeRequest request){

        ApplicationUser applicationUser = validateRequest(userMail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        userSettings.setSortingMode(request.getSortingMode());

        filterSettingsRepository.save(userSettings);

    }

    public void addFilterPriorities(String userMail, PriorityRequest priorities){

        ApplicationUser applicationUser = validateRequest(userMail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        for(String priority : priorities.getPriorities()){

            if(!userSettings.getPriorities().contains(priority)){

                userSettings.getPriorities().add(priority);

            }

        }

        filterSettingsRepository.save(userSettings);

    }

    public void deleteFilterPriority(String userMail, DeletePriority priority){

        ApplicationUser applicationUser = validateRequest(userMail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        userSettings.getPriorities().remove(priority.getPriority());

        filterSettingsRepository.save(userSettings);

    }

    public void clearFilterPriorities(String userMail){

        ApplicationUser applicationUser = validateRequest(userMail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        userSettings.getPriorities().clear();

        filterSettingsRepository.save(userSettings);

    }

    public FilterSettingsResponse getFilterSettings(String mail) {

        ApplicationUser applicationUser = validateRequest(mail);

        FilterSettings userSettings = filterSettingsRepository.findByUser(applicationUser).get();

        return new FilterSettingsResponse(userSettings.isShowOnlyUnfinished(), userSettings.isShowOnlyDelegated(), userSettings.isShowOnlyWithLocation(), userSettings.getCollaboratorEmail(), userSettings.getPriorities(), userSettings.getTags(), userSettings.getSortingMode());

    }

    public void deleteUserFilters(ApplicationUser user){
        filterSettingsRepository.deleteByUser(user);
    }

}
