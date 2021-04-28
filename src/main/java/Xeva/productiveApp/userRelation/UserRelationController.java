package Xeva.productiveApp.userRelation;

import Xeva.productiveApp.userRelation.requests.AllCollaboratorsResponse;
import Xeva.productiveApp.userRelation.requests.Request;
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
    public void addCollaborator(@RequestBody Request request){
        userRelationService.addRelation(request);
    }

    @GetMapping("/getCollaborators/{mail}")
    public Set<String> getCollaborators(@PathVariable String mail){
        return userRelationService.getCollaborators(mail);
    }

    @GetMapping("/getAllCollaborators/{mail}")
    public Set<AllCollaboratorsResponse> getAllCollaborators(@PathVariable String mail){
        return userRelationService.getAllCollaborators(mail);
    }

    @PostMapping("/deleteCollaborator")
    public void deleteCollaborator(@RequestBody Request request){
        userRelationService.deleteRelation(request);
    }

}
