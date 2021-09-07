package Xeva.productiveApp.userRelation;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.task.Task;
import Xeva.productiveApp.task.TaskList;
import Xeva.productiveApp.task.TaskRepository;
import Xeva.productiveApp.userRelation.dto.AllCollaboratorsResponse;
import Xeva.productiveApp.userRelation.dto.CollaboratorNameResponse;
import Xeva.productiveApp.userRelation.dto.Request;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserRelationService {

    private final AppUserService appUserService;
    private final UserRelationRepository userRelationRepository;
    private final TaskRepository taskRepository;

    public List<Task> getCollaboratorRecentlyFinishedTasks(String userMail, String collaboratorMail, int page, int size){

        this.checkIfUserExists(userMail);
        this.checkIfUserExists(collaboratorMail);

        ApplicationUser collaborator = appUserService.findByEmail(collaboratorMail).get();
        ApplicationUser user = appUserService.findByEmail(userMail).get();

        if(userRelationRepository.findByUser1AndUser2(user, collaborator).isEmpty() && userRelationRepository.findByUser1AndUser2(collaborator, user).isEmpty()){
            throw new IllegalStateException("Relation between user's don't exist");
        }

        Pageable sortedByUpdateDate = PageRequest.of(page, size, Sort.by("lastUpdated").descending());

        return taskRepository.findAllByUserAndIfDone(collaborator, true, sortedByUpdateDate);

    }

    public List<Task> getCollaboratorActiveTasks(String userMail, String collaboratorMail, int page, int size){

        this.checkIfUserExists(userMail);
        this.checkIfUserExists(collaboratorMail);

        ApplicationUser collaborator = appUserService.findByEmail(collaboratorMail).get();
        ApplicationUser user = appUserService.findByEmail(userMail).get();

        if(userRelationRepository.findByUser1AndUser2(user, collaborator).isEmpty() && userRelationRepository.findByUser1AndUser2(collaborator, user).isEmpty()){
            throw new IllegalStateException("Relation between user's don't exist");
        }

        Pageable paginationRequest = PageRequest.of(page, size);

        return taskRepository.getUserActiveTasks(collaborator, TaskList.ANYTIME, TaskList.SCHEDULED, paginationRequest);

    }

    public int getNumberOfCollaboratorActiveTasks(String userMail, String collaboratorMail){

        this.checkIfUserExists(userMail);
        this.checkIfUserExists(collaboratorMail);

        ApplicationUser collaborator = appUserService.findByEmail(collaboratorMail).get();
        ApplicationUser user = appUserService.findByEmail(userMail).get();

        if(userRelationRepository.findByUser1AndUser2(user, collaborator).isEmpty() && userRelationRepository.findByUser1AndUser2(collaborator, user).isEmpty()){
            throw new IllegalStateException("Relation between user's don't exist");
        }

        List<Task> activeTasks = taskRepository.getUserActiveTasks(collaborator, TaskList.ANYTIME, TaskList.SCHEDULED);

        return activeTasks.size();

    }

    public int getNumberOfCollaboratorFinishedTasks(String userMail, String collaboratorMail){

        this.checkIfUserExists(userMail);
        this.checkIfUserExists(collaboratorMail);

        ApplicationUser collaborator = appUserService.findByEmail(collaboratorMail).get();
        ApplicationUser user = appUserService.findByEmail(userMail).get();

        if(userRelationRepository.findByUser1AndUser2(user, collaborator).isEmpty() && userRelationRepository.findByUser1AndUser2(collaborator, user).isEmpty()){
            throw new IllegalStateException("Relation between user's don't exist");
        }

        List<Task> finishedTasks = taskRepository.findAllByUserAndIfDone(collaborator, true);

        return finishedTasks.size();

    }

    public void acceptAskForPermission(String userMail, String collaboratorMail){

        this.checkIfUserExists(userMail);
        this.checkIfUserExists(collaboratorMail);

        ApplicationUser collaborator = appUserService.findByEmail(collaboratorMail).get();
        ApplicationUser user = appUserService.findByEmail(userMail).get();

        if(userRelationRepository.findByUser1AndUser2(user, collaborator).isEmpty() && userRelationRepository.findByUser1AndUser2(collaborator, user).isEmpty()){
            throw new IllegalStateException("Relation between user's don't exist");
        }

        UserRelation userRelation;

        if(userRelationRepository.findByUser1AndUser2(user, collaborator).isPresent()){
            userRelation = userRelationRepository.findByUser1AndUser2(user, collaborator).get();
            userRelation.setUser2AskForPermission(false);
            userRelation.setUser2Permission(true);
        }else{
            userRelation = userRelationRepository.findByUser1AndUser2(collaborator, user).get();
            userRelation.setUser1AskForPermission(false);
            userRelation.setUser1Permission(true);
        }

        userRelationRepository.save(userRelation);

    }

    public void declineAskForPermission(String userMail, String collaboratorMail){

        this.checkIfUserExists(userMail);
        this.checkIfUserExists(collaboratorMail);

        ApplicationUser collaborator = appUserService.findByEmail(collaboratorMail).get();
        ApplicationUser user = appUserService.findByEmail(userMail).get();

        if(userRelationRepository.findByUser1AndUser2(user, collaborator).isEmpty() && userRelationRepository.findByUser1AndUser2(collaborator, user).isEmpty()){
            throw new IllegalStateException("Relation between user's don't exist");
        }

        UserRelation userRelation;

        if(userRelationRepository.findByUser1AndUser2(user, collaborator).isPresent()){
            userRelation = userRelationRepository.findByUser1AndUser2(user, collaborator).get();
            userRelation.setUser2AskForPermission(false);
        }else{
            userRelation = userRelationRepository.findByUser1AndUser2(collaborator, user).get();
            userRelation.setUser1AskForPermission(false);
        }

        userRelationRepository.save(userRelation);

    }

    public void askForPermission(String userMail, String collaboratorMail){

        this.checkIfUserExists(userMail);
        this.checkIfUserExists(collaboratorMail);

        ApplicationUser collaborator = appUserService.findByEmail(collaboratorMail).get();
        ApplicationUser user = appUserService.findByEmail(userMail).get();

        if(userRelationRepository.findByUser1AndUser2(user, collaborator).isEmpty() && userRelationRepository.findByUser1AndUser2(collaborator, user).isEmpty()){
            throw new IllegalStateException("Relation between user's don't exist");
        }

        UserRelation userRelation;

        if(userRelationRepository.findByUser1AndUser2(user, collaborator).isPresent()){
            userRelation = userRelationRepository.findByUser1AndUser2(user, collaborator).get();
            userRelation.setUser1AskForPermission(true);
        }else{
            userRelation = userRelationRepository.findByUser1AndUser2(collaborator, user).get();
            userRelation.setUser2AskForPermission(true);
        }

        userRelationRepository.save(userRelation);

    }

    public void checkIfUserExists(String mail){
        boolean userExists = appUserService.findByEmail(mail).isPresent();

        if(!userExists){
            throw new IllegalStateException("Wrong email");
        }
    }

    public CollaboratorNameResponse getCollaboratorName(String user1Email, String user2Email){

        this.checkIfUserExists(user1Email);
        this.checkIfUserExists(user2Email);

        ApplicationUser user1 = appUserService.findByEmail(user1Email).get();
        ApplicationUser user2 = appUserService.findByEmail(user2Email).get();

        CollaboratorNameResponse collaboratorNameResponse = new CollaboratorNameResponse();

        if(userRelationRepository.findByUser1AndUser2(user1, user2).isPresent()){
            collaboratorNameResponse.setCollaboratorName(userRelationRepository.findByUser1AndUser2(user1, user2).get().getUser2().getFirstName() + ' ' + userRelationRepository.findByUser1AndUser2(user1, user2).get().getUser2().getLastName());
        } else if(userRelationRepository.findByUser1AndUser2(user2, user1).isPresent()){
            collaboratorNameResponse.setCollaboratorName(userRelationRepository.findByUser1AndUser2(user2, user1).get().getUser1().getFirstName() + ' ' + userRelationRepository.findByUser1AndUser2(user2, user1).get().getUser1().getLastName());
        } else{
            collaboratorNameResponse.setCollaboratorName(" ");
        }

        return collaboratorNameResponse;

    }

    public void changePermission(String userMail, String collaboratorMail){

        this.checkIfUserExists(userMail);
        this.checkIfUserExists(collaboratorMail);

        ApplicationUser user = appUserService.findByEmail(userMail).get();
        ApplicationUser collaborator = appUserService.findByEmail(collaboratorMail).get();

        UserRelation relation;

        if(userRelationRepository.findByUser1AndUser2(user, collaborator).isPresent()){
            relation = userRelationRepository.findByUser1AndUser2(user,collaborator).get();
        }else{
            relation = userRelationRepository.findByUser1AndUser2(collaborator, user).get();
        }

        if(relation.getUser1().getEmail().equals(collaboratorMail)){
            relation.setUser1Permission(!relation.isUser1Permission());
        }else{
            relation.setUser2Permission(!relation.isUser2Permission());
        }

        this.userRelationRepository.save(relation);

    }

    public Long addRelation(Request request){

        if(request.getCollaboratorEmail().equals(request.getUserEmail())){
            throw new IllegalStateException("Cannot invite yourself");
        }

        checkIfUserExists(request.getUserEmail());
        checkIfUserExists(request.getCollaboratorEmail());

        ApplicationUser user1 = appUserService.findByEmail(request.getUserEmail()).get();
        ApplicationUser user2 = appUserService.findByEmail(request.getCollaboratorEmail()).get();

        UserRelation newRelation = new UserRelation(user1, user2);

        userRelationRepository.save(newRelation);

        return newRelation.getId();

    }

    public void deleteRelation(Long id){

        if(userRelationRepository.findById(id).isPresent()) {

            UserRelation relationToDelete = userRelationRepository.findById(id).get();

            userRelationRepository.delete(relationToDelete);

        }else{
            throw new IllegalStateException("Given relation doesn't exists");
        }

    }

    public Set<AllCollaboratorsResponse> getAllCollaborators(String mail){

        checkIfUserExists(mail);

        ApplicationUser appUser = appUserService.findByEmail(mail).get();

        Set<UserRelation> allUserRelations = userRelationRepository.findAllByUser1(appUser).get();
        allUserRelations.addAll(userRelationRepository.findAllByUser2(appUser).get());

        Set<AllCollaboratorsResponse> result = new HashSet<>();

        for(UserRelation relation : allUserRelations){

            String user1Name = " ";
            String user2Name = " ";

            if(relation.getUser1().getFirstName() != null && relation.getUser1().getLastName() != null){
                user1Name = relation.getUser1().getFirstName() + " " + relation.getUser1().getLastName();
            }

            if(relation.getUser2().getFirstName() != null && relation.getUser2().getLastName() != null){
                user2Name = relation.getUser2().getFirstName() + " " + relation.getUser2().getLastName();
            }

            AllCollaboratorsResponse newEntry = new AllCollaboratorsResponse(relation.getId(), relation.getUser1().getEmail(), relation.getUser2().getEmail(), user1Name, user2Name, relation.getState().toString(), relation.isUser1Permission(), relation.isUser2Permission(), relation.isUser1AskForPermission(), relation.isUser2AskForPermission(), relation.getLastUpdated());

            result.add(newEntry);

        }

        return result;

    }

    public UserRelation getUsersRelation(ApplicationUser user1, ApplicationUser user2){
        if(userRelationRepository.findByUser1AndUser2(user1, user2).isPresent()) {
            return userRelationRepository.findByUser1AndUser2(user1, user2).get();
        }else if(userRelationRepository.findByUser1AndUser2(user2, user1).isPresent()){
            return userRelationRepository.findByUser1AndUser2(user2, user1).get();
        }
        else{
            return null;
        }
    }

    public Set<String> getCollaborators(String mail){

        checkIfUserExists(mail);

        ApplicationUser user = appUserService.findByEmail(mail).get();

        List<UserRelation> foundUsers = user.getListOfCollaborators();

        Set<String> result = new HashSet<>();

        for(UserRelation relation : foundUsers){
            if(relation.getUser1().getEmail().equals(mail)){
                result.add(relation.getUser2().getEmail());
            } else{
                if(relation.getState() == RelationState.ACCEPTED) {
                    result.add(relation.getUser1().getEmail());
                }
            }
        }

        return result;

    }

    public void acceptInvitation(Long id){

        UserRelation relation;

        if(userRelationRepository.findById(id).isPresent()) {

            relation = userRelationRepository.findById(id).get();

            relation.setState(RelationState.ACCEPTED);

            userRelationRepository.save(relation);

        }else{
            throw new IllegalStateException("Given relation doesn't exists");
        }

    }

    public void declineInvitation(Long id){

        UserRelation relation;

        if(userRelationRepository.findById(id).isPresent()){

            relation = userRelationRepository.findById(id).get();

            //relation.setState(RelationState.DECLINED);
            userRelationRepository.delete(relation);

            userRelationRepository.save(relation);

        }else{
            throw new IllegalStateException("Given relation doesn't exists");
        }

    }

    public void deleteAllUserRelations(ApplicationUser user){
        userRelationRepository.deleteByUser1OrUser2(user, user);
    }

    public void save(UserRelation userRelation) {
        this.userRelationRepository.save(userRelation);
    }

    public boolean relationAlreadyExist(String mail, String collaboratorName) {
        ApplicationUser user1 = new ApplicationUser();
        ApplicationUser user2 = new ApplicationUser();

        if(this.appUserService.findByEmail(mail).isPresent()){
            user1 = this.appUserService.findByEmail(mail).get();
        }
        if(this.appUserService.findByEmail(collaboratorName).isPresent()){
            user2 = this.appUserService.findByEmail(collaboratorName).get();
        }

        return this.userRelationRepository.findByUser1AndUser2(user1, user2).isPresent() || this.userRelationRepository.findByUser1AndUser2(user2, user1).isPresent();
    }

    public UserRelation findById(Long id) {
        return this.userRelationRepository.findById(id).get();
    }
}
