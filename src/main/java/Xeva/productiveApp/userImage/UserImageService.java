package Xeva.productiveApp.userImage;

import Xeva.productiveApp.appUser.AppUserRepository;
import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class UserImageService {

    private final UserImageRepository userImageRepository;
    private final AppUserRepository appUserRepository;

    public UserImage getUserImage(ApplicationUser user){
        return this.userImageRepository.findUserImageByUser(user);
    }

    public boolean checkIfExists(String userEmail) {
        boolean isValid = appUserRepository.findByEmail(userEmail).isPresent();

        if (!isValid) {
            throw new IllegalStateException("User doesn't exists");
        }

        ApplicationUser user = appUserRepository.findByEmail(userEmail).get();

        if (this.userImageRepository.findAllByUser(user).isPresent()) {
            List<UserImage> userImage = this.userImageRepository.findAllByUser(user).get();

            if (userImage.get(0).getImage() == null) {
                return false;
            }

            return userImage.get(0).getImage().length > 0;
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
        UserImage userImage = userImageRepository.findUserImageByUser(user);

        if(userImage == null) {
            userImage = new UserImage();
            userImage.setUser(user);
        }

        userImage.setImage(bytes);

        userImageRepository.save(userImage);
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
