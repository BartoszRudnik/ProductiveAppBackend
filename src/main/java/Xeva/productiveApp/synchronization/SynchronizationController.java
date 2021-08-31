package Xeva.productiveApp.synchronization;

import Xeva.productiveApp.synchronization.dto.SynchronizeTagsRequest;
import Xeva.productiveApp.tags.Tag;
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
    public Set<Tag> synchronizeTags(@PathVariable String mail, @RequestBody List<SynchronizeTagsRequest> requestList){
        return this.synchronizationService.synchronizeTags(mail, requestList);
    }

}
