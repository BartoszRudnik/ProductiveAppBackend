package Xeva.productiveApp.userRelation;

import Xeva.productiveApp.userRelation.dto.AllCollaboratorsResponse;
import Xeva.productiveApp.userRelation.dto.Request;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/acceptInvitation/{id}")
    public void acceptInvitation(@PathVariable Long id){
        userRelationService.acceptInvitation(id);
    }

    @PutMapping("/declineInvitation/{id}")
    public void declineInvitation(@PathVariable Long id){
        userRelationService.declineInvitation(id);
    }

    @GetMapping("/getCollaborators/{mail}")
    public Set<String> getCollaborators(@PathVariable String mail){
        return userRelationService.getCollaborators(mail);
    }

    @GetMapping("/getAllCollaborators/{mail}")
    public Set<AllCollaboratorsResponse> getAllCollaborators(@PathVariable String mail){
        return userRelationService.getAllCollaborators(mail);
    }

    @DeleteMapping("/deleteCollaborator/{id}")
    public void deleteCollaborator(@PathVariable Long id){
        userRelationService.deleteRelation(id);
    }

}
