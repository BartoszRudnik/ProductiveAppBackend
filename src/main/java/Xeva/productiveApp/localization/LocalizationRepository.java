package Xeva.productiveApp.localization;

import Xeva.productiveApp.appUser.ApplicationUser;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocalizationRepository extends JpaRepository<Localization, Long> {

    List<Localization> findAllByUser(ApplicationUser user);

    Optional<Localization> findTopByCountryAndLocalityAndStreetAndLatitudeAndLongitudeAndUser(String country, String locality, String street, Float latitude, Float longitude, ApplicationUser user);

    Optional<Localization> findByIdAndUser(Long id, ApplicationUser user);
    Optional<Localization> findByLocalizationNameAndUser(String localizationName, ApplicationUser user);

    @Transactional
    void deleteAllByUser(ApplicationUser user);

    @Transactional
    void deleteById(Long id);

    @Transactional
    void deleteByUserAndLocalizationName(ApplicationUser user, String localizationName);

    @Transactional
    void deleteByUuid(String uuid);

    Optional<Localization> findByUuid(String uuid);
}
