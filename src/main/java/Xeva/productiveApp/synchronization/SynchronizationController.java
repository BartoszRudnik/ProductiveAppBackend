package Xeva.productiveApp.synchronization;

import Xeva.productiveApp.localization.Localization;
import Xeva.productiveApp.synchronization.dto.*;
import Xeva.productiveApp.tags.Tag;
import Xeva.productiveApp.userRelation.dto.AllCollaboratorsResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/synchronize")
@AllArgsConstructor
public class SynchronizationController {

    private final SynchronizationService synchronizationService;

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
