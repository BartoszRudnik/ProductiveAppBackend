package Xeva.productiveApp.version;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/version")
@AllArgsConstructor
public class VersionController {

    @GetMapping("/getVersion")
    public String getVersion(){
        return "0.0.1";
    }

}
