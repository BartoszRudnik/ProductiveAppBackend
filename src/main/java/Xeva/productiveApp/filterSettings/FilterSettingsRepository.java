package Xeva.productiveApp.filterSettings;

import Xeva.productiveApp.appUser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilterSettingsRepository extends JpaRepository<FilterSettings, Long> {

    Optional<FilterSettings> findByUser(ApplicationUser user);

}
