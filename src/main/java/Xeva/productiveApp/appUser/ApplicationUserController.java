package Xeva.productiveApp.appUser;

import Xeva.productiveApp.appUser.dto.GetUserDataRequest;
import Xeva.productiveApp.appUser.dto.UpdateUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/userData")
@AllArgsConstructor
public class ApplicationUserController {

    private final AppUserService appUserService;

    @PostMapping("/update/{userMail}")
    public void updateUserData(@PathVariable String userMail, @RequestBody UpdateUserRequest request){
        appUserService.updateUserData(userMail, request);
    }

    @PutMapping("/clear/{userMail}")
    public void clearUserData(@PathVariable String userMail){
        appUserService.clearUserData(userMail);
    }

    @GetMapping("/get/{userMail}")
    public GetUserDataRequest getUserData(@PathVariable String userMail){
        return appUserService.getUserData(userMail);
    }

}
