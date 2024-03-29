package Xeva.productiveApp.graphicBackground;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.graphicBackground.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

        BackgroundResponse response;

        if(appUser.getGraphicBackground() != null){
            response = new BackgroundResponse(appUser.getGraphicBackground().getBackgroundType().toString());
        }else{
            response = new BackgroundResponse(BackgroundType.GREY.toString());
        }

        return response;

    }

    public void saveBackground(BackgroundRequest body){
        boolean isUser = appUserService.findByEmail(body.getUserMail()).isPresent();

        if(!isUser){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserService.findByEmail(body.getUserMail()).get();

        GraphicBackground background;

        if(this.graphicBackgroundRepository.findTopByBackgroundType(this.getBackgroundType(body.getBackgroundType())).isPresent()){
            background = this.graphicBackgroundRepository.findTopByBackgroundType(this.getBackgroundType(body.getBackgroundType())).get();
        }else{
            background = new GraphicBackground(this.getBackgroundType(body.getBackgroundType()));
        }

        user.setLastUpdatedGraphic(LocalDateTime.now());

        background.addUser(user);
        graphicBackgroundRepository.save(background);
    }

    public void saveBackground(String mode, ApplicationUser user){
        GraphicBackground background;

        if(this.graphicBackgroundRepository.findTopByBackgroundType(this.getBackgroundType(mode)).isPresent()){
            background = this.graphicBackgroundRepository.findTopByBackgroundType(this.getBackgroundType(mode)).get();
        }else{
            background = new GraphicBackground(this.getBackgroundType(mode));
        }

        user.setLastUpdatedGraphic(LocalDateTime.now());

        background.addUser(user);
        graphicBackgroundRepository.save(background);
    }


    private BackgroundType getBackgroundType(String type){

        if ("GREY".equals(type)) {
            return BackgroundType.GREY;
        }

        return BackgroundType.BLACK;

    }

}
