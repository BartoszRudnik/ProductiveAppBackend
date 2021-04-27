package Xeva.productiveApp.userRelation;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.userRelation.requests.AllCollaboratorsResponse;
import Xeva.productiveApp.userRelation.requests.Request;
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

    public void addRelation(Request request){

        checkIfUserExists(request.getUserEmail());
        checkIfUserExists(request.getCollaboratorEmail());

        ApplicationUser user1 = appUserService.findByEmail(request.getUserEmail()).get();
        ApplicationUser user2 = appUserService.findByEmail(request.getCollaboratorEmail()).get();

        UserRelation newRelation = new UserRelation(user1, user2);
        UserRelation newRelation2 = new UserRelation(user2, user1);

        userRelationRepository.save(newRelation);
        userRelationRepository.save(newRelation2);

    }

    public void deleteRelation(Request request){

        checkIfUserExists(request.getCollaboratorEmail());
        checkIfUserExists(request.getUserEmail());

        ApplicationUser user1 = appUserService.findByEmail(request.getUserEmail()).get();
        ApplicationUser user2 = appUserService.findByEmail(request.getCollaboratorEmail()).get();

        UserRelation relationToDelete = userRelationRepository.findByUser1AndUser2(user1, user2).get();
        UserRelation relationToDelete2 = userRelationRepository.findByUser1AndUser2(user2, user1).get();

        userRelationRepository.delete(relationToDelete);
        userRelationRepository.delete(relationToDelete2);

    }

    public Set<AllCollaboratorsResponse> getAllCollaborators(String mail){

        checkIfUserExists(mail);

        ApplicationUser appUser = appUserService.findByEmail(mail).get();

        List<UserRelation> allUserRelations = appUser.getListOfCollaborators();

        Set<AllCollaboratorsResponse> result = new HashSet<>();

        for(UserRelation relation : allUserRelations){

            AllCollaboratorsResponse newEntry;

            if(relation.getUser1().getEmail().equals(mail)) {
                newEntry = new AllCollaboratorsResponse(relation.getUser2().getEmail(), relation.getState().toString());
            }else{
                newEntry = new AllCollaboratorsResponse(relation.getUser1().getEmail(), relation.getState().toString());
            }

            result.add(newEntry);

        }

        return result;

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

}
