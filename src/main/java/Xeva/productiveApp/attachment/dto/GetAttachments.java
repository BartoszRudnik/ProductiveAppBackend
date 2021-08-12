package Xeva.productiveApp.attachment.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetAttachments {

    Long id;
    Long taskId;
    String fileName;

}
