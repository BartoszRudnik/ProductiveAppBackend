package Xeva.productiveApp.userRelation;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.userRelation.dto.AllCollaboratorsResponse;
import Xeva.productiveApp.userRelation.dto.Request;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserRelationService {

    private final AppUserService appUserService;
    private final UserRelationRepository userRelationRepository;

    public void checkIfUserExists(String mail){
        boolean userExists = appUserService.findByEmail(mail).isPresent();

        if(!userExists){
            throw new IllegalStateException("Wrong email");
        }
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

        return newRelation.getId_userRelation();

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

            AllCollaboratorsResponse newEntry = new AllCollaboratorsResponse(relation.getId_userRelation(), relation.getUser1().getEmail(), relation.getUser2().getEmail(), relation.getState().toString());

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

            relation.setState(RelationState.DECLINED);

            userRelationRepository.save(relation);

        }else{
            throw new IllegalStateException("Given relation doesn't exists");
        }

    }

    public void deleteAllUserRelations(ApplicationUser user){
        userRelationRepository.deleteByUser1OrUser2(user, user);
    }

}
