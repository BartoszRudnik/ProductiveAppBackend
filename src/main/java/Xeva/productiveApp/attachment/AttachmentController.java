package Xeva.productiveApp.attachment;

import Xeva.productiveApp.attachment.dto.GetAttachments;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/attachment")
@AllArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/addAttachment/{userMail}/{taskId}/{fileName}")
    public Long addAttachment(@PathVariable Long taskId, @PathVariable String fileName, @PathVariable String userMail , @RequestParam MultipartFile multipartFile) throws IOException {
        return this.attachmentService.addAttachment(multipartFile, taskId, userMail, fileName);
    }

    @GetMapping("/getAttachment/{attachmentId}")
    public Resource getAttachment(@PathVariable Long attachmentId){
        return this.attachmentService.getAttachment(attachmentId);
    }

    @GetMapping("/getUserAttachments/{userMail}")
    public List<GetAttachments> getUserAttachments(@PathVariable String userMail){
        return this.attachmentService.getUserAttachments(userMail);
    }

    @DeleteMapping("/deleteAttachment/{attachmentId}")
    public void deleteAttachment(@PathVariable Long attachmentId){
        this.attachmentService.deleteAttachment(attachmentId);
    }

}

