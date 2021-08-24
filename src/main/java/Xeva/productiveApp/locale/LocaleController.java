package Xeva.productiveApp.locale;


import Xeva.productiveApp.locale.dto.LocaleRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/locale")
@AllArgsConstructor
public class LocaleController {

    private final LocaleService localeService;

    @PostMapping("/set/{email}")
    public void setLocale(@PathVariable String email, @RequestBody LocaleRequest request){
        this.localeService.setLocale(request, email);
    }

    @GetMapping("/get/{email}")
    public LocaleRequest getLocale(@PathVariable String email){
        return this.localeService.getLocale(email);
    }

}
