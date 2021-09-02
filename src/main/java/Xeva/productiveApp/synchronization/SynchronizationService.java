package Xeva.productiveApp.synchronization;
import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.locale.Locale;
import Xeva.productiveApp.locale.LocaleService;
import Xeva.productiveApp.localization.Localization;
import Xeva.productiveApp.localization.LocalizationRepository;
import Xeva.productiveApp.localization.LocalizationService;
import Xeva.productiveApp.synchronization.dto.*;
import Xeva.productiveApp.tags.Tag;
import Xeva.productiveApp.tags.TagRepository;
import Xeva.productiveApp.tags.TagService;
import Xeva.productiveApp.userRelation.RelationState;
import Xeva.productiveApp.userRelation.UserRelation;
import Xeva.productiveApp.userRelation.UserRelationRepository;
import Xeva.productiveApp.userRelation.UserRelationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class SynchronizationService {

    private final TagService tagService;
    private final TagRepository tagRepository;
    private final LocalizationService localizationService;
    private final LocalizationRepository localizationRepository;
    private final UserRelationService userRelationService;
    private final UserRelationRepository userRelationRepository;
    private final AppUserService appUserService;
    private final LocaleService localeService;

    public void synchronizeLocale(String mail, SynchronizeLocaleRequest request){
        ApplicationUser applicationUser = this.appUserService.findByEmail(mail).get();

        if(applicationUser.getLocale() == null){
            this.localeService.setLocale(request.getLocale(), applicationUser);
        }else{
            if(applicationUser.getLastUpdated().isBefore(request.getLastUpdated())){
                this.localeService.setLocale(request.getLocale(), applicationUser);
            }
        }
    }

    public void synchronizeCollaborators(String mail, SynchronizeCollaboratorsRequestList requestList){
        ApplicationUser user1 = this.appUserService.findByEmail(mail).get();

        for(SynchronizeCollaboratorsRequest collaborator : requestList.getCollaboratorList()){
            if(!this.userRelationService.relationAlreadyExist(mail, collaborator.getEmail())){

                ApplicationUser user2 = this.appUserService.findByEmail(collaborator.getEmail()).get();

                if(user1 != user2) {
                    UserRelation userRelation = new UserRelation(user1, user2);
                    this.userRelationService.save(userRelation);
                }
            }else{
                ApplicationUser user2 = this.appUserService.findByEmail(collaborator.getEmail()).get();
                UserRelation existingRelation;

                if(this.userRelationRepository.findByUser1AndUser2(user1, user2).isPresent()){
                    existingRelation = this.userRelationRepository.findByUser1AndUser2(user1, user2).get();
                }else{
                    existingRelation = this.userRelationRepository.findByUser1AndUser2(user2, user1).get();
                }

                if(existingRelation.getLastUpdated().isBefore(collaborator.getLastUpdated())){

                    RelationState state;

                    if(collaborator.getRelationState().equals("ACCEPTED")){
                        state = RelationState.ACCEPTED;
                    }else if(collaborator.getRelationState().equals("DECLINED")){
                        state = RelationState.DECLINED;
                    }else{
                        state = RelationState.WAITING;
                    }

                    existingRelation.setState(state);

                    if(existingRelation.getUser1().getEmail().equals(mail)){
                        existingRelation.setUser2Permission(collaborator.isSentPermission());
                        existingRelation.setUser2AskForPermission(collaborator.isAskingForPermission());
                        existingRelation.setUser1Permission(collaborator.isReceivedPermission());
                        existingRelation.setUser1AskForPermission(collaborator.isAlreadyAsked());
                    }else{
                        existingRelation.setUser1Permission(collaborator.isSentPermission());
                        existingRelation.setUser1AskForPermission(collaborator.isAskingForPermission());
                        existingRelation.setUser2Permission(collaborator.isReceivedPermission());
                        existingRelation.setUser2AskForPermission(collaborator.isAlreadyAsked());
                    }

                    this.userRelationService.save(existingRelation);
                }
            }
        }

        List<DeleteCollaborator> toDelete = requestList.getDeleteList();

        for(DeleteCollaborator delete : toDelete){
            ApplicationUser appUser1 = this.appUserService.findByEmail(delete.getUser1Mail()).get();
            ApplicationUser appUser2 = this.appUserService.findByEmail(delete.getUser2Mail()).get();
            if(this.userRelationRepository.findByUser1AndUser2(appUser1, appUser2).isPresent()){
                this.userRelationRepository.deleteByUser1AndUser2(appUser1, appUser2);
            }else if(this.userRelationRepository.findByUser1AndUser2(appUser2, appUser1).isPresent()){
                this.userRelationRepository.deleteByUser1AndUser2(appUser2, appUser1);
            }
        }
    }

    public void synchronizeLocations(String mail, SynchronizeLocationsRequestList requestList){
        for(SynchronizeLocationsRequest location : requestList.getLocationList()){
            if(!this.localizationService.localizationAlreadyExist(mail, location.getLocalizationName())){
                ApplicationUser user = this.appUserService.findByEmail(mail).get();

                Localization newLocalization = new Localization(location.getLocalizationName(), location.getStreet(), location.getLocality(),location.getCountry(), location.getLongitude(),location.getLatitude(), user);

                this.localizationService.save(newLocalization);
            }else{
                Localization existingLocalization = this.localizationService.findById(location.getId()).get();

                if(existingLocalization.getLastUpdated().isBefore(location.getLastUpdated())){

                    existingLocalization.setLocalizationName(location.getLocalizationName());
                    existingLocalization.setLocality(location.getLocality());
                    existingLocalization.setCountry(location.getCountry());
                    existingLocalization.setLatitude(location.getLatitude());
                    existingLocalization.setLongitude(location.getLongitude());
                    existingLocalization.setStreet(location.getStreet());

                    this.localizationService.save(existingLocalization);
                }
            }
        }

        List<DeleteLocation> listToDelete = requestList.getDeleteList();

        for(DeleteLocation toDelete : listToDelete){
            ApplicationUser user = this.appUserService.findByEmail(toDelete.getOwnerEmail()).get();
            this.localizationRepository.deleteByUserAndLocalizationName(user, toDelete.getLocationName());
        }
    }

    public void synchronizeTags(String mail, SynchronizeTagsRequestList requestList){
        for(SynchronizeTagsRequest tag : requestList.getTagList()){

            if(!this.tagService.tagAlreadyExist(mail, tag.getId())){

                Tag newTag = new Tag(mail, tag.getName());
                this.tagService.save(newTag);
            }else{
                Tag existingTag = this.tagRepository.findById(tag.getId()).get();

                if(existingTag.getLastUpdated().isBefore(tag.getLastUpdated())){
                    existingTag.setName(tag.getName());

                    this.tagService.save(existingTag);
                }
            }
        }

        List<DeleteTag> listToDelete = requestList.getDeleteList();

        for(DeleteTag toDelete : listToDelete){
            this.tagService.deleteByName(toDelete.getTagName(), toDelete.getOwnerEmail());
        }
    }

}
