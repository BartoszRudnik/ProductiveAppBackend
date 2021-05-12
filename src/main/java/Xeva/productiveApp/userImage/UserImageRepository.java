package Xeva.productiveApp.userImage;

import Xeva.productiveApp.appUser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    UserImage findUserImageByUser(ApplicationUser user);

    @Transactional
    void deleteByUser(ApplicationUser user);

}
