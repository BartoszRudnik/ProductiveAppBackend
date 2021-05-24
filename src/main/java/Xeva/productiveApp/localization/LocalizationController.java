package Xeva.productiveApp.localization;

import Xeva.productiveApp.localization.dto.AddLocalization;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/localization")
@AllArgsConstructor
public class LocalizationController {

    private final LocalizationService localizationService;

    @GetMapping("/getLocalizations/{mail}")
    public List<Localization> getLocalizations(@PathVariable String mail){
        return localizationService.getLocalizations(mail);
    }

    @GetMapping("/getLocalization/{id}")
    public Localization getLocalization(@PathVariable Long id) {
        return localizationService.getLocalization(id);
    }

    @PostMapping("/addLocalization/{mail}")
    public Long addLocalization(@PathVariable String mail, @RequestBody AddLocalization addLocalization){
        return localizationService.addLocalization(mail, addLocalization);
    }

    @DeleteMapping("/deleteLocalization/{id}")
    public void deleteLocalization(@PathVariable Long id){
        localizationService.deleteLocalization(id);
    }

    @PutMapping("/updateLocalization/{id}")
    public void updateLocalization(@PathVariable Long id, @RequestBody AddLocalization addLocalization){
        localizationService.updateLocalization(id, addLocalization);
    }

}
