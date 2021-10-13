package Xeva.productiveApp.synchronization;
import Xeva.productiveApp.synchronization.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/synchronize")
@AllArgsConstructor
public class SynchronizationController {

    private final SynchronizationService synchronizationService;

    @PostMapping("/synchronizeTasks/{mail}")
    public void synchronizeTasks(@PathVariable String mail, @RequestBody SynchronizeTaskRequestList request){
       this.synchronizationService.synchronizeTasks(mail, request);
    }

    @PostMapping("/synchronizeSettings/{mail}")
    public void synchronizeSettings(@PathVariable String mail, @RequestBody SynchronizeSettingsRequest request){
        this.synchronizationService.synchronizeSettings(mail, request);
    }

    @PostMapping("/synchronizeUser")
    public void synchronizeUser(@RequestBody SynchronizeUserRequest request){
        this.synchronizationService.synchronizeUser(request);
    }

    @PostMapping("/synchronizeGraphic/{mail}")
    public void synchronizeGraphic(@PathVariable String mail,@RequestBody SynchronizeGraphicRequest request){
        this.synchronizationService.synchronizeGraphic(mail, request);
    }

    @PostMapping("/synchronizeLocale/{mail}")
    public void synchronizeLocale(@PathVariable String mail, @RequestBody SynchronizeLocaleRequest request){
        this.synchronizationService.synchronizeLocale(mail, request);
    }

    @PostMapping("/synchronizeTags/{mail}")
    public void synchronizeTags(@PathVariable String mail, @RequestBody SynchronizeTagsRequestList requestList){
        this.synchronizationService.synchronizeTags(mail, requestList);
    }

    @PostMapping("/synchronizeLocations/{mail}")
    public void synchronizeLocations(@PathVariable String mail, @RequestBody SynchronizeLocationsRequestList requestList){
       this.synchronizationService.synchronizeLocations(mail, requestList);
    }

    @PostMapping("/synchronizeCollaborators/{mail}")
    public void synchronizeCollaborators(@PathVariable String mail, @RequestBody SynchronizeCollaboratorsRequestList requestList){
       this.synchronizationService.synchronizeCollaborators(mail, requestList);
    }

}
