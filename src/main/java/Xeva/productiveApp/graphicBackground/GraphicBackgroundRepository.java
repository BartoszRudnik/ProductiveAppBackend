package Xeva.productiveApp.graphicBackground;

import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.filterSettings.FilterSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface GraphicBackgroundRepository extends JpaRepository<GraphicBackground, Long> {

    Optional<GraphicBackground> findByUser(ApplicationUser user);

}
