package Xeva.productiveApp.task;

import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.localization.Localization;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndUser(Long id, ApplicationUser user);

    Optional<List<Task>> findAllByUserEmail(String email);

    Optional<List<Task>> findAllByNotificationLocalization(Localization notificationLocalization);

    @Transactional
    void deleteAllByUser(ApplicationUser user);

    @Transactional
    @Query("SELECT t FROM Task t where t.user = ?1 AND t.ifDone = false AND (t.taskList = ?2 or t.taskList = ?3)")
    List<Task> getUserActiveTasks(ApplicationUser user, TaskList taskList1, TaskList taskList2, Pageable page);

    @Transactional
    @Query("SELECT t FROM Task t where t.user = ?1 AND t.ifDone = false AND (t.taskList = ?2 or t.taskList = ?3)")
    List<Task> getUserActiveTasks(ApplicationUser user, TaskList taskList1, TaskList taskList2);

    List<Task> findAllByUserAndIfDone(ApplicationUser user, boolean ifDone, Pageable page);

    List<Task> findAllByUserAndIfDone(ApplicationUser user, boolean ifDone);

}
