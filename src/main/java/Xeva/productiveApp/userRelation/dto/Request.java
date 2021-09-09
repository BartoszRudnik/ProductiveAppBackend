package Xeva.productiveApp.userRelation.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Request {

    String userEmail;
    String collaboratorEmail;
    String uuid;

}
