package Xeva.productiveApp.userRelation;

import Xeva.productiveApp.task.Task;
import Xeva.productiveApp.userRelation.dto.AllCollaboratorsResponse;
import Xeva.productiveApp.userRelation.dto.CollaboratorNameResponse;
import Xeva.productiveApp.userRelation.dto.Request;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/delegate")
@AllArgsConstructor
public class UserRelationController {

    private final UserRelationService userRelationService;

    @PostMapping("/addCollaborator")
    public Long addCollaborator(@RequestBody Request request){
        return userRelationService.addRelation(request);
    }

    @PutMapping("/acceptInvitation/{uuid}")
    public void acceptInvitation(@PathVariable String uuid){
        userRelationService.acceptInvitation(uuid);
    }

    @PutMapping("/declineInvitation/{uuid}")
    public void declineInvitation(@PathVariable String uuid){
        userRelationService.declineInvitation(uuid);
    }

    @PostMapping("/changePermission/{userMail}/{collaboratorMail}")
    public void changePermission(@PathVariable String userMail, @PathVariable String collaboratorMail){
        userRelationService.changePermission(userMail, collaboratorMail);
    }

    @PostMapping("/askForPermission/{userMail}/{collaboratorMail}")
    public void askForPermission(@PathVariable String userMail, @PathVariable String collaboratorMail){
        userRelationService.askForPermission(userMail, collaboratorMail);
    }

    @PostMapping("/declineAskForPermission/{userMail}/{collaboratorMail}")
    public void declineAskForPermission(@PathVariable String userMail, @PathVariable String collaboratorMail){
        userRelationService.declineAskForPermission(userMail, collaboratorMail);
    }

    @PostMapping("/acceptAskForPermission/{userMail}/{collaboratorMail}")
    public void acceptAskForPermission(@PathVariable String userMail, @PathVariable String collaboratorMail){
        userRelationService.acceptAskForPermission(userMail, collaboratorMail);
    }

    @GetMapping("/getCollaboratorRecentlyFinished/{userMail}/{collaboratorMail}/{page}/{size}")
    public List<Task> getCollaboratorRecentlyFinishedTasks(
            @PathVariable String userMail,
            @PathVariable String collaboratorMail,
            @PathVariable int page,
            @PathVariable int size){
        return userRelationService.getCollaboratorRecentlyFinishedTasks(userMail, collaboratorMail, page, size);
    }

    @GetMapping("/getNumberOfCollaboratorFinishedTasks/{userMail}/{collaboratorMail}")
    public int getNumberOfCollaboratorFinishedTasks(@PathVariable String userMail ,@PathVariable String collaboratorMail) {
        return userRelationService.getNumberOfCollaboratorFinishedTasks(userMail, collaboratorMail);
    }

    @GetMapping("/getNumberOfCollaboratorActiveTasks/{userMail}/{collaboratorMail}")
    public int getNumberOfCollaboratorActiveTasks(@PathVariable String userMail, @PathVariable String collaboratorMail){
        return userRelationService.getNumberOfCollaboratorActiveTasks(userMail, collaboratorMail);
    }

    @GetMapping("/getCollaboratorActiveTasks/{userMail}/{collaboratorMail}/{page}/{size}")
    public List<Task> getCollaboratorActiveTasks(
            @PathVariable String userMail,
            @PathVariable String collaboratorMail,
            @PathVariable int page,
            @PathVariable int size){
        return userRelationService.getCollaboratorActiveTasks(userMail, collaboratorMail, page, size);
    }

    @GetMapping("/getCollaboratorName/{userMail}/{collaboratorMail}")
    public CollaboratorNameResponse getCollaboratorName(@PathVariable String userMail, @PathVariable String collaboratorMail){
        return userRelationService.getCollaboratorName(userMail, collaboratorMail);
    }

    @GetMapping("/getCollaborators/{mail}")
    public Set<String> getCollaborators(@PathVariable String mail){
        return userRelationService.getCollaborators(mail);
    }

    @GetMapping("/getAllCollaborators/{mail}")
    public Set<AllCollaboratorsResponse> getAllCollaborators(@PathVariable String mail){
        return userRelationService.getAllCollaborators(mail);
    }

    @DeleteMapping("/deleteCollaborator/{uuid}")
    public void deleteCollaborator(@PathVariable String uuid){
        userRelationService.deleteRelation(uuid);
    }

}
