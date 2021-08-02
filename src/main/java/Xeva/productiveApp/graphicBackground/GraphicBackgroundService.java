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

    public void saveAll(List<GraphicBackground> graphicBackgrounds){
        graphicBackgroundRepository.saveAll(graphicBackgrounds);
    }

    public void save(GraphicBackground graphicBackground){
        graphicBackgroundRepository.save(graphicBackground);
    }

    public requestBody getBackground(String userMail){
        boolean isUser = appUserService.findByEmail(userMail).isPresent();

        if(!isUser){
            throw new IllegalStateException("User doesn't exists");
        }
        ApplicationUser appUser= appUserService.findByEmail(userMail).get();
        GraphicBackground background = graphicBackgroundRepository.findByUser(appUser).get();
        return new requestBody(background.getUser().getEmail(), background.getBackgroundType().toString());
    }

    public void updateBackground(responseBody body){
        boolean isUser = appUserService.findByEmail(body.getUserMail()).isPresent();

        if(!isUser){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserService.findByEmail(body.getUserMail()).get();
        GraphicBackground background = new GraphicBackground(user, getBackgroundType(body.getBackgroundType()));
        graphicBackgroundRepository.save(background);
    }
    private BackgroundType getBackgroundType(String type){
        switch(type){
            case "BLACK":
                return BackgroundType.BLACK;
            case "GREY":
                return BackgroundType.GREY;
            default:
                return BackgroundType.BLACK;
        }
    }
}
