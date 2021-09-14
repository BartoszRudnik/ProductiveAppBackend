package Xeva.productiveApp.userImage;

import Xeva.productiveApp.appUser.AppUserRepository;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.userImage.dto.LastUpdatedResponse;
import lombok.AllArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserImageService {

    private final UserImageRepository userImageRepository;
    private final AppUserRepository appUserRepository;

    public LastUpdatedResponse getLastUpdated(String userMail){
        if(this.appUserRepository.findByEmail(userMail).isPresent()){
            ApplicationUser user= this.appUserRepository.findByEmail(userMail).get();

            if(this.userImageRepository.findTopByUser(user).isPresent()){
                UserImage userImage = this.userImageRepository.findTopByUser(user).get();

                return new LastUpdatedResponse(userImage.getLastUpdated());
            }else{
             return null;
            }
        }else{
            throw new IllegalStateException("User doesn't exist");
        }
    }

    public UserImage getUserImage(ApplicationUser user){
        return this.userImageRepository.findUserImageByUser(user);
    }

    public boolean checkIfExists(String userEmail) {
        boolean isValid = appUserRepository.findByEmail(userEmail).isPresent();

        if (!isValid) {
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserRepository.findByEmail(userEmail).get();

        if (this.userImageRepository.findTopByUser(user).isPresent()) {
            UserImage userImage = this.userImageRepository.findTopByUser(user).get();

            if (userImage.getImage() == null) {
                return false;
            }

            return userImage.getImage().length > 0;
        }
        else{
            return false;
        }
    }

    public Resource getImage(String userEmail){
        boolean isValid = appUserRepository.findByEmail(userEmail).isPresent();

        if(!isValid){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserRepository.findByEmail(userEmail).get();

        UserImage userImage = userImageRepository.findUserImageByUser(user);

        if(userImage != null && userImage.getImage() != null) {
            byte[] image = userImage.getImage();

            return new ByteArrayResource(image);
        }else{
            return null;
        }
    }

    public void setImage(byte [] bytes, ApplicationUser user){

        this.userImageRepository.deleteAllByUser(user);

        UserImage userImage = new UserImage();
        userImage.setUser(user);
        userImage.setLastUpdated(LocalDateTime.now());
        userImage.setImage(bytes);

        this.userImageRepository.save(userImage);
    }

    public LocalDateTime setImage(MultipartFile multipartFile, String userEmail) throws IOException {
        boolean isValid = appUserRepository.findByEmail(userEmail).isPresent();

        if(!isValid){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserRepository.findByEmail(userEmail).get();

        this.userImageRepository.deleteAllByUser(user);

        UserImage userImage = new UserImage();
        userImage.setUser(user);
        userImage.setLastUpdated(LocalDateTime.now());
        userImage.setImage(multipartFile.getBytes());

        this.userImageRepository.save(userImage);

        return userImage.getLastUpdated();
    }

    public void deleteImage(String userEmail){
        boolean isValid = appUserRepository.findByEmail(userEmail).isPresent();

        if(!isValid){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserRepository.findByEmail(userEmail).get();

        userImageRepository.deleteAllByUser(user);
    }

}
