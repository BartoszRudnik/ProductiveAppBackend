package Xeva.productiveApp.synchronization;

import Xeva.productiveApp.localization.Localization;
import Xeva.productiveApp.synchronization.dto.SynchronizeCollaboratorsRequestList;
import Xeva.productiveApp.synchronization.dto.SynchronizeLocationsRequestList;
import Xeva.productiveApp.synchronization.dto.SynchronizeTagsRequest;
import Xeva.productiveApp.synchronization.dto.SynchronizeTagsRequestList;
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

    @PostMapping("/synchronizeTags/{mail}")
    public Set<Tag> synchronizeTags(@PathVariable String mail, @RequestBody SynchronizeTagsRequestList requestList){
        return this.synchronizationService.synchronizeTags(mail, requestList);
    }

    @PostMapping("/synchronizeLocations/{mail}")
    public List<Localization> synchronizeLocations(@PathVariable String mail, @RequestBody SynchronizeLocationsRequestList requestList){
        return this.synchronizationService.synchronizeLocations(mail, requestList);
    }

    @PostMapping("/synchronizeCollaborators/{mail}")
    public Set<AllCollaboratorsResponse> synchronizeCollaborators(@PathVariable String mail, @RequestBody SynchronizeCollaboratorsRequestList requestList){
        return this.synchronizationService.synchronizeCollaborators(mail, requestList);
    }

}
