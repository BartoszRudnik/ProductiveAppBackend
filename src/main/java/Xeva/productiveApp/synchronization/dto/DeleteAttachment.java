package Xeva.productiveApp.synchronization.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeleteAttachment {

    private Long attachmentId;
    private String fileName;

}
