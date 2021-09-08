package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeAttachmentRequest {

    private Long id;
    private Long taskId;
    private String fileName;
    private boolean toDelete;
    private LocalDateTime lastUpdated;
    private byte [] localFile;

}
