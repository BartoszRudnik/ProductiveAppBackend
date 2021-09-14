package Xeva.productiveApp.userImage;

import Xeva.productiveApp.userImage.dto.LastUpdatedResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/userImage")
@AllArgsConstructor
public class UserImageController {

    private final UserImageService userImageService;

    @GetMapping("/getLastUpdated/{userMail}")
    public LastUpdatedResponse getLastUpdated(@PathVariable String userMail) {
        return this.userImageService.getLastUpdated(userMail);
    }

    @PostMapping("/setImage/{userMail}")
    public LocalDateTime setImage(@RequestParam MultipartFile multipartFile, @PathVariable String userMail) throws IOException {
        return this.userImageService.setImage(multipartFile, userMail);
    }

    @PostMapping("/deleteImage/{userMail}")
    public void deleteImage(@PathVariable String userMail){
        userImageService.deleteImage(userMail);
    }

    @GetMapping("/getImage/{userMail}")
    public Resource getImage(@PathVariable String userMail){
        return userImageService.getImage(userMail);
    }

    @GetMapping("/checkIfExists/{userMail}")
    public boolean checkIfExists(@PathVariable String userMail){
        return userImageService.checkIfExists(userMail);
    }

}
