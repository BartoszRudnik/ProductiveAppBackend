package Xeva.productiveApp.deleteAccount;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/account")
@AllArgsConstructor
public class DeleteAccountController {

    private final DeleteAccountService deleteAccount;

    @PostMapping("/deleteAccount/{userMail}/{token}")
    public void deleteAccount(@PathVariable String userMail, @PathVariable String token){
        deleteAccount.deleteAccount(userMail, token);
    }

    @PostMapping("/deleteAccountToken/{userMail}")
    public void deleteAccountToken(@PathVariable String userMail){
        deleteAccount.deleteAccountToken(userMail);
    }

}
