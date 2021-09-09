package Xeva.productiveApp.attachment;

import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Transactional
    Optional<Attachment> findByUuid(String uuid);

    @Transactional
    Optional<Attachment> findByAttachmentIdAndFileNameAndApplicationUser(Long attachmentId, String fileName, ApplicationUser applicationUser);

    @Transactional
    List<Attachment> findAllByApplicationUser(ApplicationUser applicationUser);

    @Transactional
    List<Attachment> findAllByTaskId(Long taskId);

    @Transactional
    void deleteAllByApplicationUser(ApplicationUser applicationUser);

    @Transactional
    void deleteAllByTaskId(Long taskId);

    @Transactional
    void deleteByUuid(String uuid);

}
