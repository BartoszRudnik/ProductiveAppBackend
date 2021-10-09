package Xeva.productiveApp.attachment.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetAttachments {

    private Long id;
    private String taskUuid;
    private String fileName;
    private String uuid;

}
