package Xeva.productiveApp.synchronization;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.localization.Localization;
import Xeva.productiveApp.localization.LocalizationService;
import Xeva.productiveApp.synchronization.dto.*;
import Xeva.productiveApp.tags.Tag;
import Xeva.productiveApp.tags.TagRepository;
import Xeva.productiveApp.tags.TagService;
import Xeva.productiveApp.userRelation.RelationState;
import Xeva.productiveApp.userRelation.UserRelation;
import Xeva.productiveApp.userRelation.UserRelationService;
import Xeva.productiveApp.userRelation.dto.AllCollaboratorsResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SynchronizationService {

    private final TagService tagService;
    private final TagRepository tagRepository;
    private final LocalizationService localizationService;
    private final UserRelationService userRelationService;
    private final AppUserService appUserService;

    public Set<AllCollaboratorsResponse>synchronizeCollaborators(String mail, SynchronizeCollaboratorsRequestList requestList){
        for(SynchronizeCollaboratorsRequest collaborator : requestList.getCollaboratorList()){
            if(!this.userRelationService.relationAlreadyExist(mail, collaborator.getCollaboratorName())){

                ApplicationUser user1 = this.appUserService.findByEmail(mail).get();
                ApplicationUser user2 = this.appUserService.findByEmail(collaborator.getEmail()).get();

                UserRelation userRelation = new UserRelation(user1, user2);

                this.userRelationService.save(userRelation);
            }else{
                UserRelation existingRelation = this.userRelationService.findById(collaborator.getId());

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

        return this.userRelationService.getAllCollaborators(mail);
    }

    public List<Localization> synchronizeLocations(String mail, SynchronizeLocationsRequestList requestList){
        for(SynchronizeLocationsRequest location : requestList.getLocationList()){
            if(!this.localizationService.localizationAlreadyExist(mail, location.getId())){
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

        return this.localizationService.getLocalizations(mail);
    }

    public Set<Tag> synchronizeTags(String mail, SynchronizeTagsRequestList requestList){

        List<String> namesFromDevice = new ArrayList<>();

        for(SynchronizeTagsRequest tag : requestList.getTagList()){
            namesFromDevice.add(tag.getName());

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

        Set<Tag> updatedTags = this.tagService.findAllByEmail(mail);

        for(Tag tag : updatedTags){
            if(!namesFromDevice.contains(tag.getName())){
                this.tagService.deleteById(tag.getId());
            }
        }

        return this.tagService.findAllByEmail(mail);
    }

}
