package Xeva.productiveApp.task;

import Xeva.productiveApp.appUser.ApplicationUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<List<Task>> findAllByUserEmail(String email);

    @Transactional
    void deleteAllByUser(ApplicationUser user);

    List<Task> findAllByUserAndLocalizationOrLocalization(
            ApplicationUser user,
            TaskLocalization localization,
            TaskLocalization localization2,
            Pageable page);

    List<Task> findAllByUserAndLocalizationOrLocalization(
            ApplicationUser user,
            TaskLocalization localization,
            TaskLocalization localization2);

    List<Task> findAllByUserAndIfDone(ApplicationUser user, boolean ifDone, Pageable page);

    List<Task> findAllByUserAndIfDone(ApplicationUser user, boolean ifDone);

}
