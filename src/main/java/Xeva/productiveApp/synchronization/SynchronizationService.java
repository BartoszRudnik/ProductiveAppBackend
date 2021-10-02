package Xeva.productiveApp.synchronization;
import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.attachment.AttachmentService;
import Xeva.productiveApp.filterSettings.FilterSettings;
import Xeva.productiveApp.filterSettings.FilterSettingsService;
import Xeva.productiveApp.graphicBackground.GraphicBackgroundService;
import Xeva.productiveApp.locale.LocaleService;
import Xeva.productiveApp.localization.Localization;
import Xeva.productiveApp.localization.LocalizationService;
import Xeva.productiveApp.synchronization.dto.*;
import Xeva.productiveApp.tags.Tag;
import Xeva.productiveApp.tags.TagRepository;
import Xeva.productiveApp.tags.TagService;
import Xeva.productiveApp.task.Task;
import Xeva.productiveApp.task.TaskList;
import Xeva.productiveApp.task.TaskPriority;
import Xeva.productiveApp.task.TaskService;
import Xeva.productiveApp.userImage.UserImage;
import Xeva.productiveApp.userImage.UserImageService;
import Xeva.productiveApp.userRelation.RelationState;
import Xeva.productiveApp.userRelation.UserRelation;
import Xeva.productiveApp.userRelation.UserRelationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SynchronizationService {
    private final TagService tagService;
    private final TagRepository tagRepository;
    private final LocalizationService localizationService;
    private final UserRelationService userRelationService;
    private final AppUserService appUserService;
    private final LocaleService localeService;
    private final GraphicBackgroundService graphicBackgroundService;
    private final UserImageService userImageService;
    private final FilterSettingsService filterSettingsService;
    private final TaskService taskService;
    private final AttachmentService attachmentService;

    public void synchronizeTasks(String mail, SynchronizeTaskRequestList request){
        List<SynchronizeTaskRequest> taskToSynchronize = request.getTaskList();
        List<SynchronizeAttachmentRequest> attachments = request.getAttachmentList();

        ApplicationUser user = this.appUserService.findByEmail(mail).get();

        for(SynchronizeTaskRequest t : taskToSynchronize){

            List<String> tagList = new ArrayList<>();

            if(t.getTags() != null){
                tagList = List.of(t.getTags().split("\\|"));
            }

            TaskList taskList = this.taskService.getLocalization(t.getLocalization());
            TaskPriority taskPriority = this.taskService.getPriority(t.getPriority());

            if(!this.taskService.isPresent(t.getUuid())){

                this.taskService.addTask(user, taskPriority, taskList, tagList, t.getDelegatedEmail(), t.getNotificationLocalizationUuid(), t.getTitle(), t.getDescription(), t.isDone(), t.getStartDate(), t.getEndDate(), t.getUuid(), t.getNotificationLocalizationRadius(), t.isNotificationOnEnter(), t.isNotificationOnExit(), t.getTaskState());

            }else{
                Task existingTask = this.taskService.findByUuid(t.getUuid());

                if(existingTask != null && existingTask.getLastUpdated().isBefore(t.getLastUpdated())){
                    this.taskService.updateTask(tagList, t.getPosition(), t.getUuid(), t.getDescription(), t.getTitle(), t.getStartDate(), t.getEndDate(), t.getPriority(), t.isDone(), t.getLocalization(), t.isCanceled(), t.getNotificationLocalizationUuid(), t.getNotificationLocalizationRadius(), t.isNotificationOnEnter(), t.isNotificationOnExit(), t.getDelegatedEmail(), t.getTaskState());
                }
            }
        }

        for(DeleteTask taskToDelete : request.getDeleteList()){
            if(this.taskService.isPresent(taskToDelete.getUuid())) {
                this.taskService.deleteTaskByUuid(taskToDelete.getUuid());
            }
        }

        for(SynchronizeAttachmentRequest attachment : attachments){
            if(!this.attachmentService.alreadyExist(attachment.getUuid())){
                System.out.println(attachment.getTaskUuid());
                this.attachmentService.addAttachment(attachment.getLocalFile(), attachment.getTaskUuid(), user, attachment.getFileName(), attachment.getUuid());
            }
        }

        for(DeleteAttachment deleteAttachment : request.getDeleteListAttachments()){
           if(this.attachmentService.alreadyExist(deleteAttachment.getUuid())){
                this.attachmentService.deleteByUuid(deleteAttachment.getUuid());
            }
        }
    }

    public void synchronizeSettings(String mail, SynchronizeSettingsRequest request){
        ApplicationUser user = this.appUserService.findByEmail(mail).get();

        if(!this.filterSettingsService.settingsExist(user)){
            FilterSettings filterSettings = new FilterSettings(user, request.isShowOnlyDelegated(), request.isShowOnlyWithLocalization(), request.getCollaborators(), request.getPriorities(), request.getTags(), request.getLocations(), request.getSortingMode());

            this.filterSettingsService.save(filterSettings);
        }else{
            FilterSettings filterSettings = this.filterSettingsService.findByUser(user);

            if(filterSettings != null && filterSettings.getLastUpdated().isBefore(request.getLastUpdated())){
                filterSettings.setShowDelegated(request.isShowOnlyDelegated());
                filterSettings.setShowWithLocation(request.isShowOnlyWithLocalization());
                filterSettings.setSortingMode(request.getSortingMode());
                filterSettings.setCollaboratorEmail(request.getCollaborators());
                filterSettings.setPriorities(request.getPriorities());
                filterSettings.setTags(request.getTags());
                filterSettings.setLocations(request.getLocations());

                this.filterSettingsService.save(filterSettings);
            }
        }
    }

    public void synchronizeUser(SynchronizeUserRequest request){
        ApplicationUser user = this.appUserService.findByEmail(request.getEmail()).get();

        if(!this.userImageService.checkIfExists(request.getEmail()) && request.getLocalImage().length > 0){

            this.userImageService.setImage(request.getLocalImage(), user);
        }else{
            UserImage userImage = this.userImageService.getUserImage(user);

            if(userImage.getLastUpdated().isBefore(request.getLastUpdatedImage()) && request.getLocalImage().length > 0){

                this.userImageService.setImage(request.getLocalImage(), user);
            }
        }

        if(user.getLastUpdateName().isBefore(request.getLastUpdatedName()) || (user.getLastName() == null && user.getFirstName() == null)){
            this.appUserService.updateUserData(user, request.getFirstName(), request.getLastName());
        }
    }

    public void synchronizeGraphic(String mail, SynchronizeGraphicRequest request) {
        ApplicationUser applicationUser = this.appUserService.findByEmail(mail).get();

        if(applicationUser.getGraphicBackground() == null){
            this.graphicBackgroundService.saveBackground(request.getMode(), applicationUser);
        }else{
            if(applicationUser.getLastUpdatedGraphic().isBefore(request.getLastUpdated())){
                this.graphicBackgroundService.saveBackground(request.getMode(), applicationUser);
            }
        }
    }

    public void synchronizeLocale(String mail, SynchronizeLocaleRequest request){
        ApplicationUser applicationUser = this.appUserService.findByEmail(mail).get();

        if(applicationUser.getLocale() == null){
            this.localeService.setLocale(request.getLocale(), applicationUser);
        }else{
            if(applicationUser.getLastUpdatedLocale().isBefore(request.getLastUpdated())){
                this.localeService.setLocale(request.getLocale(), applicationUser);
            }
        }
    }

    public void synchronizeCollaborators(String mail, SynchronizeCollaboratorsRequestList requestList){
        ApplicationUser user1 = this.appUserService.findByEmail(mail).get();

        for(SynchronizeCollaboratorsRequest collaborator : requestList.getCollaboratorList()){
            ApplicationUser user2 = this.appUserService.findByEmail(collaborator.getEmail()).get();
            if(!this.userRelationService.relationAlreadyExist(collaborator.getUuid())){
                if(user1 != user2) {
                    UserRelation userRelation = new UserRelation(user1, user2, collaborator.getUuid());
                    this.userRelationService.save(userRelation);
                }
            }else{
                UserRelation existingRelation = this.userRelationService.findByUuid(collaborator.getUuid());

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
            this.userRelationService.deleteByUuid(delete.getUuid());
        }
    }

    public void synchronizeLocations(String mail, SynchronizeLocationsRequestList requestList){
        for(SynchronizeLocationsRequest location : requestList.getLocationList()){
            if(!this.localizationService.localizationAlreadyExist(location.getUuid())){
                ApplicationUser user = this.appUserService.findByEmail(mail).get();

                Localization newLocalization = new Localization(location.getLocalizationName(), location.getStreet(), location.getLocality(),location.getCountry(), location.getLongitude(),location.getLatitude(), user, location.getUuid(), location.getSaved());

                this.localizationService.save(newLocalization);
            }else{
                Localization existingLocalization = this.localizationService.findByUuid(location.getUuid());

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
            this.localizationService.deleteLocalization(toDelete.getUuid());
        }
    }

    public void synchronizeTags(String mail, SynchronizeTagsRequestList requestList){
        for(SynchronizeTagsRequest tag : requestList.getTagList()){

            if(!this.tagService.tagAlreadyExist(mail, tag.getId())){

                Tag newTag = new Tag(mail, tag.getName(), tag.getUuid());
                this.tagService.save(newTag);
            }else{
                Tag existingTag = this.tagRepository.findById(tag.getId()).get();

                if(existingTag.getLastUpdated().isBefore(tag.getLastUpdated())){
                    this.tagService.updateTag(mail, existingTag.getName(), tag.getName());
                }
            }
        }

        List<DeleteTag> listToDelete = requestList.getDeleteList();

        for(DeleteTag toDelete : listToDelete){
            this.tagService.deleteByName(toDelete.getTagName(), toDelete.getOwnerEmail());
        }
    }
}
