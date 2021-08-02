package Xeva.productiveApp.graphicBackground;

import Xeva.productiveApp.graphicBackground.dto.BackgroundRequest;
import Xeva.productiveApp.graphicBackground.dto.BackgroundResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/themeMode")
@AllArgsConstructor
public class GraphicBackgroundController {

    private final GraphicBackgroundService graphicBackgroundService;

    @PostMapping("/add")
    public void setGraphicBackground(@RequestBody BackgroundRequest request){
        this.graphicBackgroundService.saveBackground(request);
    }

    @GetMapping("/get/{mail}")
    public BackgroundResponse getGraphicBackground(@PathVariable String mail){
        return this.graphicBackgroundService.getBackground(mail);
    }

}
