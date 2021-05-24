package Xeva.productiveApp.deleteAccount;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.email.EmailSender;
import Xeva.productiveApp.filterSettings.FilterSettingsService;
import Xeva.productiveApp.localization.LocalizationService;
import Xeva.productiveApp.passwordReset.resetToken.ResetToken;
import Xeva.productiveApp.passwordReset.resetToken.ResetTokenService;
import Xeva.productiveApp.registration.confirmationToken.ConfirmationTokenService;
import Xeva.productiveApp.tags.TagService;
import Xeva.productiveApp.task.TaskService;
import Xeva.productiveApp.userImage.UserImageService;
import Xeva.productiveApp.userRelation.UserRelationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class DeleteAccountService {

    private final TagService tagService;
    private final TaskService taskService;
    private final FilterSettingsService filterSettingsService;
    private final UserRelationService userRelationService;
    private final UserImageService userImageService;
    private final ResetTokenService resetTokenService;
    private final AppUserService appUserService;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;
    private final LocalizationService localizationService;

    private boolean checkTokenExpiryDate(ResetToken token){
        return token.getExpiresAt().isAfter(LocalDateTime.now());
    }

    public void deleteAccountToken(String userMail){

        boolean isUser = appUserService.findByEmail(userMail).isPresent();

        if(!isUser){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserService.findByEmail(userMail).get();

        String code = this.generateResetToken();

        ResetToken token = new ResetToken(code, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);

        resetTokenService.saveResetToken(token);

        emailSender.send("Delete account", userMail, emailSender.buildEmailAccountDelete(user.getFirstName(), code));

    }

    public void deleteAccount(String userMail, String token){

        boolean isUser = appUserService.findByEmail(userMail).isPresent();

        if(!isUser){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserService.findByEmail(userMail).get();
        ResetToken tokenFromDB = resetTokenService.getTokenByTokenAndUser(user, token);

        if(!tokenFromDB.getToken().equals(token)){
            throw new IllegalStateException("Wrong token");
        }

        if(!this.checkTokenExpiryDate(tokenFromDB)){
            throw new IllegalStateException("Token has expired");
        }

        localizationService.deleteAllUserLocalizations(user);
        tagService.deleteByUser(userMail);
        taskService.deleteAll(user);
        filterSettingsService.deleteUserFilters(user);
        userRelationService.deleteAllUserRelations(user);
        userImageService.deleteImage(userMail);
        resetTokenService.deleteUserTokens(user);
        confirmationTokenService.deleteConfirmationToken(user);
        appUserService.deleteUser(user);

    }

    private String generateResetToken(){

        int tokenLen = 6;

        String digits = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(tokenLen);

        for(int i = 0; i < tokenLen; i++) {
            stringBuilder.append(digits.charAt(random.nextInt(digits.length())));
        }

        return stringBuilder.toString();

    }

}
