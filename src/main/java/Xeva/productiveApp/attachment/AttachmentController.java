package Xeva.productiveApp.attachment;

import Xeva.productiveApp.attachment.dto.DelegatedAttachments;
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

    @PostMapping("/addAttachment/{userMail}/{taskUuid}/{fileName}/{uuid}")
    public Long addAttachment(@PathVariable String taskUuid, @PathVariable String uuid, @PathVariable String fileName, @PathVariable String userMail , @RequestParam MultipartFile multipartFile) throws IOException {
        return this.attachmentService.addAttachment(multipartFile, taskUuid, userMail, fileName, uuid);
    }

    @GetMapping("/getAttachment/{uuid}")
    public Resource getAttachment(@PathVariable String uuid){
        return this.attachmentService.getAttachment(uuid);
    }

    @PostMapping("/getDelegatedAttachments")
    public List<GetAttachments> getDelegatedAttachments(@RequestBody DelegatedAttachments delegatedAttachments){
        return this.attachmentService.getDelegatedAttachments(delegatedAttachments);
    }

    @GetMapping("/getUserAttachments/{userMail}")
    public List<GetAttachments> getUserAttachments(@PathVariable String userMail){
        return this.attachmentService.getUserAttachments(userMail);
    }

    @DeleteMapping("/deleteAttachment/{uuid}")
    public void deleteAttachment(@PathVariable String uuid){
        this.attachmentService.deleteByUuid(uuid);
    }

}

