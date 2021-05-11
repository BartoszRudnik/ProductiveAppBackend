package Xeva.productiveApp.filterSettings.pojo;

import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CollaboratorEmailRequest {

    private List<String> collaboratorEmail;

}