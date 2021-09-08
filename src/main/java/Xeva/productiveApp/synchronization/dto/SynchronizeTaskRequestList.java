package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeTaskRequestList {

    private List<SynchronizeTaskRequest> taskList;
    private List<SynchronizeAttachmentRequest> attachmentList;
    private List<DeleteTask> deleteList;
    private List<DeleteAttachment> deleteListAttachments;

}
