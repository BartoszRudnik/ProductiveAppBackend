package Xeva.productiveApp.graphicBackground;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.graphicBackground.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GraphicBackgroundService {

    private final AppUserService appUserService;
    private final GraphicBackgroundRepository graphicBackgroundRepository;

    public BackgroundResponse getBackground(String userMail){

        boolean isUser = appUserService.findByEmail(userMail).isPresent();

        if(!isUser){
            throw new IllegalStateException("User doesn't exists");
        }
        ApplicationUser appUser= appUserService.findByEmail(userMail).get();

        if(this.graphicBackgroundRepository.findByUser(appUser).isPresent()) {

            GraphicBackground background = graphicBackgroundRepository.findByUser(appUser).get();

            System.out.println(background.getBackgroundType().toString());

            return new BackgroundResponse(background.getBackgroundType().toString());
        }else{
            return new BackgroundResponse(BackgroundType.GREY.toString());
        }

    }

    public void saveBackground(BackgroundRequest body){

        boolean isUser = appUserService.findByEmail(body.getUserMail()).isPresent();

        if(!isUser){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserService.findByEmail(body.getUserMail()).get();
        GraphicBackground background = new GraphicBackground(user, this.getBackgroundType(body.getBackgroundType()));

        graphicBackgroundRepository.save(background);

    }

    private BackgroundType getBackgroundType(String type){

        if ("GREY".equals(type)) {
            return BackgroundType.GREY;
        }

        return BackgroundType.BLACK;

    }

    public void deleteUserBackground(ApplicationUser user) {graphicBackgroundRepository.deleteByUser(user);}
}
