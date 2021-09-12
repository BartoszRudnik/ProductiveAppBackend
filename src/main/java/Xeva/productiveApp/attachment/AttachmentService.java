package Xeva.productiveApp.attachment;

import Xeva.productiveApp.appUser.AppUserRepository;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.attachment.dto.DelegatedAttachments;
import Xeva.productiveApp.attachment.dto.GetAttachments;
import Xeva.productiveApp.task.Task;
import Xeva.productiveApp.task.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final AppUserRepository appUserRepository;

    @Transactional
    public void deleteByUuid(String uuid){
        if(this.attachmentRepository.findByUuid(uuid).isEmpty()){
            throw new IllegalStateException("Attachment doesn't exist");
        }

        this.attachmentRepository.deleteByUuid(uuid);
    }

    @Transactional
    public boolean alreadyExist(String uuid){
        return this.attachmentRepository.findByUuid(uuid).isPresent();
    }

    public Long addAttachment(MultipartFile multipartFile, String taskUuid, String userMail, String fileName, String uuid) throws IOException {
        if(this.appUserRepository.findByEmail(userMail).isEmpty()){
            throw new IllegalStateException("User doesn't exist");
        }

        if(this.taskRepository.findByUuid(taskUuid).isEmpty()){
            throw new IllegalStateException("Task doesn't exist");
        }

        ApplicationUser applicationUser = this.appUserRepository.findByEmail(userMail).get();

        Task task = this.taskRepository.findByUuid(taskUuid).get();

        Attachment attachment = new Attachment(multipartFile.getBytes(), null, applicationUser, fileName, uuid);

        task.addAttachment(attachment);

        this.taskRepository.save(task);
        this.attachmentRepository.save(attachment);

        return attachment.getAttachmentId();
    }

    @Transactional
    public void addAttachment(byte [] fileBytes, String taskUuid, ApplicationUser user, String fileName, String uuid) {
        if(this.taskRepository.findByUuid(taskUuid).isEmpty()){
            throw new IllegalStateException("Task doesn't exist");
        }

        Task task = this.taskRepository.findByUuid(taskUuid).get();

        Attachment attachment = new Attachment(fileBytes, null, user, fileName, uuid);

        task.addAttachment(attachment);

        this.attachmentRepository.save(attachment);
        this.taskRepository.save(task);
    }

    public Resource getAttachment(String uuid){

        if(this.attachmentRepository.findByUuid(uuid).isEmpty()){
            throw new IllegalStateException("Attachment doesn't exist");
        }

        Attachment attachment = this.attachmentRepository.findByUuid(uuid).get();

        return new ByteArrayResource(attachment.getFile());

    }

    public List<GetAttachments> getUserAttachments(String userMail){

        if(this.appUserRepository.findByEmail(userMail).isEmpty()){
            throw new IllegalStateException("User doesn't exist");
        }

        ApplicationUser applicationUser = this.appUserRepository.findByEmail(userMail).get();

        List<Attachment> attachments = this.attachmentRepository.findAllByApplicationUser(applicationUser);

        List<GetAttachments> result = new ArrayList<>();

        for(Attachment attachment : attachments){
            GetAttachments getAttachments = new GetAttachments(attachment.getAttachmentId(), attachment.getTask().getUuid(), attachment.getFileName(), attachment.getUuid());

            result.add(getAttachments);
        }

        return result;

    }

    public List<GetAttachments> getDelegatedAttachments(DelegatedAttachments attachments){
        List<GetAttachments> result = new ArrayList<>();

        for(int i = 0; i < attachments.getTasksId().size(); i++){
              List<Attachment> attachment = this.attachmentRepository.findAllByTaskId(attachments.getTasksId().get(i));

              if(attachment != null) {
                  for(Attachment singleAttachment: attachment) {
                      GetAttachments getAttachments = new GetAttachments(singleAttachment.getAttachmentId(), singleAttachment.getTask().getUuid(), singleAttachment.getFileName(), singleAttachment.getUuid());
                      result.add(getAttachments);
                  }
              }
        }

        return result;
    }

    public void deleteUserAttachments(ApplicationUser applicationUser){
        if(this.appUserRepository.findByEmail(applicationUser.getEmail()).isEmpty()){
            throw new IllegalStateException("User doesn't exist");
        }

        this.attachmentRepository.deleteAllByApplicationUser(applicationUser);
    }

    @Transactional
    public void deleteAttachment(Long attachmentId){

        if(this.attachmentRepository.findById(attachmentId).isEmpty()){
            throw new IllegalStateException("Attachment doesn't exist");
        }

        this.attachmentRepository.deleteById(attachmentId);

    }

}
