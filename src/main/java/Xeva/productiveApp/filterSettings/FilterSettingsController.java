package Xeva.productiveApp.filterSettings;

import Xeva.productiveApp.filterSettings.pojo.FilterSettingsResponse;
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

    @PostMapping("/changeShowOnlyUnfinishedStatus/{mail}")
    public void changeShowOnlyUnfinishedStatus(@PathVariable String mail){
        filterSettingsService.changeShowOnlyUnfinished(mail);
    }

}
