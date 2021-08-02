package Xeva.productiveApp.localization;

import Xeva.productiveApp.appUser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LocalizationRepository extends JpaRepository<Localization, Long> {

    List<Localization> findAllByUser(ApplicationUser user);

    @Transactional
    void deleteAllByUser(ApplicationUser user);

    @Transactional
    void deleteById(Long id);

}
