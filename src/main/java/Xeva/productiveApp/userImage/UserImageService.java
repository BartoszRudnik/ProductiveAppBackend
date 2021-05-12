package Xeva.productiveApp.userImage;

import Xeva.productiveApp.appUser.AppUserRepository;
import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class UserImageService {

    private final UserImageRepository userImageRepository;
    private final AppUserRepository appUserRepository;

    public boolean checkIfExists(String userEmail){
        boolean isValid = appUserRepository.findByEmail(userEmail).isPresent();

        if(!isValid){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserRepository.findByEmail(userEmail).get();

        UserImage userImage = userImageRepository.findUserImageByUser(user);

        if(userImage == null){
            return false;
        }

        return userImage.getImage().length > 0;
    }

    public Resource getImage(String userEmail){
        boolean isValid = appUserRepository.findByEmail(userEmail).isPresent();

        if(!isValid){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserRepository.findByEmail(userEmail).get();

        UserImage userImage = userImageRepository.findUserImageByUser(user);

        if(userImage != null) {
            byte[] image = userImage.getImage();

            return new ByteArrayResource(image);
        }else{
            return null;
        }
    }

    public void setImage(MultipartFile multipartFile, String userEmail) throws IOException {

        boolean isValid = appUserRepository.findByEmail(userEmail).isPresent();

        if(!isValid){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserRepository.findByEmail(userEmail).get();

        UserImage userImage = userImageRepository.findUserImageByUser(user);

        if(userImage == null) {
            userImage = new UserImage();
            userImage.setUser(user);
        }

        userImage.setImage(multipartFile.getBytes());

        userImageRepository.save(userImage);

    }

    public void deleteImage(String userEmail){
        boolean isValid = appUserRepository.findByEmail(userEmail).isPresent();

        if(!isValid){
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserRepository.findByEmail(userEmail).get();

        userImageRepository.deleteByUser(user);
    }

}
