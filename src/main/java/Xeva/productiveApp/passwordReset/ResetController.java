package Xeva.productiveApp.passwordReset;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/resetToken")
@AllArgsConstructor
public class ResetController {

    private final ResetService resetService;

    @PostMapping
    public void reset(@RequestBody ResetRequest request){

        resetService.resetPassword(request);

    }

}
