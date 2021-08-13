package Xeva.productiveApp.attachment.dto;

import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DelegatedAttachments {

    List<Long> tasksId;

}
