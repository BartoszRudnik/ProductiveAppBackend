package Xeva.productiveApp.userImage;

import Xeva.productiveApp.appUser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    @Transactional
    void deleteAllByUser(ApplicationUser user);

    Optional<UserImage> findTopByUser(ApplicationUser user);

    Optional<List<UserImage>> findAllByUser(ApplicationUser user);

    UserImage findUserImageByUser(ApplicationUser user);

    @Transactional
    void deleteByUser(ApplicationUser user);

}
