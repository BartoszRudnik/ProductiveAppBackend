package Xeva.productiveApp.localization;

import Xeva.productiveApp.localization.dto.AddLocalization;
import Xeva.productiveApp.localization.dto.GetCoordinates;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/localization")
@AllArgsConstructor
public class LocalizationController {
    private final LocalizationService localizationService;

    @GetMapping("/getCoordinates/{uuid}")
    public GetCoordinates getCoordinates(@PathVariable String uuid){
        return localizationService.getCoordinates(uuid);
    }

    @GetMapping("/getLocalizations/{mail}")
    public List<Localization> getLocalizations(@PathVariable String mail){
        return localizationService.getLocalizations(mail);
    }

    @GetMapping("/getLocalization/{uuid}")
    public Localization getLocalization(@PathVariable String uuid) {
        return localizationService.findByUuid(uuid);
    }

    @PostMapping("/addLocalization/{mail}")
    public Long addLocalization(@PathVariable String mail, @RequestBody AddLocalization addLocalization){
        return localizationService.addLocalization(mail, addLocalization);
    }

    @DeleteMapping("/deleteLocalization/{uuid}")
    public void deleteLocalization(@PathVariable String uuid){
        localizationService.deleteByUuid(uuid);
    }

    @PutMapping("/updateLocalization/{uuid}")
    public void updateLocalization(@PathVariable String uuid, @RequestBody AddLocalization addLocalization){
        localizationService.updateLocalization(uuid, addLocalization);
    }
}
