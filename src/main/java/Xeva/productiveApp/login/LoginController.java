package Xeva.productiveApp.login;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/login")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public String login(@RequestBody LoginRequest request){

        System.out.println(request.getEmail() + " " + request.getPassword());

        return loginService.signIn(request);

    }

}
