package Xeva.productiveApp.attachment;

import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Transactional
    List<Attachment> findAllByApplicationUser(ApplicationUser applicationUser);

    @Transactional
    List<Attachment> findAllByTaskId(Long taskId);

    @Transactional
    void deleteAllByApplicationUser(ApplicationUser applicationUser);

    @Transactional
    void deleteAllByTaskId(Long taskId);

}
