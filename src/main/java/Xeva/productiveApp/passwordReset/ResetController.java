package Xeva.productiveApp.passwordReset;

import Xeva.productiveApp.passwordReset.resetToken.ResetTokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/resetToken")
@AllArgsConstructor
public class ResetController {

    private final ResetTokenService resetTokenService;

    @PostMapping
    public String reset(@RequestBody ResetRequest request){



        return "success";

    }

}
