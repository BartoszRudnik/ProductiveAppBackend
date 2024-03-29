package Xeva.productiveApp.filterSettings;

import Xeva.productiveApp.filterSettings.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/filterSettings")
@AllArgsConstructor
public class FilterSettingsController {

    private final FilterSettingsService filterSettingsService;

    @GetMapping("/getFilterSettings/{mail}")
    public FilterSettingsResponse getFilterSettings(@PathVariable String mail){
        return filterSettingsService.getFilterSettings(mail);
    }

    @PostMapping("/changeShowOnlyWithLocalization/{mail}")
    public void changeShowOnlyWithLocation(@PathVariable String mail){
        filterSettingsService.changeShowOnlyWithLocation(mail);
    }

    @PostMapping("/changeShowOnlyDelegatedStatus/{mail}")
    public void changeShowOnlyDelegatedStatus(@PathVariable String mail){
        filterSettingsService.changeShowOnlyDelegated(mail);
    }

    @PostMapping("/addFilterCollaboratorEmail/{mail}")
    public void addFilterCollaboratorEmail(@PathVariable String mail, @RequestBody CollaboratorEmailRequest collaboratorEmail){
        filterSettingsService.addFilterCollaboratorEmail(mail, collaboratorEmail);
    }

    @PostMapping("/addFilterLocations/{mail}")
    public void addFilterLocation(@PathVariable String mail, @RequestBody LocationRequest locationRequest){
        filterSettingsService.addFilterLocations(mail, locationRequest);
    }

    @PostMapping("/deleteFilterCollaboratorEmail/{mail}")
    public void deleteFilterCollaboratorEmail(@PathVariable String mail, @RequestBody DeleteCollaboratorEmailRequest collaboratorEmail){
        filterSettingsService.deleteFilterCollaboratorEmail(mail, collaboratorEmail);
    }

    @PostMapping("/deleteFilterLocation/{mail}")
    public void deleteFilterLocation(@PathVariable String mail, @RequestBody DeleteLocationRequest deleteLocationRequest)    {
        filterSettingsService.deleteFilterLocation(mail, deleteLocationRequest);
    }

    @PostMapping("/clearFilterLocations/{mail}")
    public void clearFilterLocations(@PathVariable String mail){
        filterSettingsService.clearFilterLocations(mail);
    }

    @PostMapping("/clearFilterCollaborators/{mail}")
    public void clearFilterCollaborators(@PathVariable String mail){
        filterSettingsService.clearFilterCollaborators(mail);
    }

    @PostMapping("/addFilterPriority/{mail}")
    public void addFilterPriority(@PathVariable String mail, @RequestBody PriorityRequest priorityRequest){
        filterSettingsService.addFilterPriorities(mail, priorityRequest);
    }

    @PostMapping("/deleteFilterPriority/{mail}")
    public void deleteFilterPriority(@PathVariable String mail, @RequestBody DeletePriority deletePriority){
        filterSettingsService.deleteFilterPriority(mail , deletePriority);
    }

    @PostMapping("/clearFilterPriorities/{mail}")
    public void clearFilterPriorities(@PathVariable String mail){
        filterSettingsService.clearFilterPriorities(mail);
    }

    @PostMapping("/addFilterTag/{mail}")
    public void addFilterTag(@PathVariable String mail, @RequestBody TagsRequest tagsRequest){
        filterSettingsService.addFilterTags(mail, tagsRequest);
    }

    @PostMapping("/deleteFilterTag/{mail}")
    public void deleteFilterTag(@PathVariable String mail, @RequestBody DeleteTag deleteTag){
        filterSettingsService.deleteFilterTag(mail, deleteTag);
    }

    @PostMapping("/clearFilterTags/{mail}")
    public void clearFilterTags(@PathVariable String mail){
        filterSettingsService.clearFilterTags(mail);
    }

    @PostMapping("/changeSortingMode/{mail}")
    public void changeSortingMode(@PathVariable String mail, @RequestBody SortingModeRequest request){
        filterSettingsService.changeSortingMode(mail, request);
    }

}
