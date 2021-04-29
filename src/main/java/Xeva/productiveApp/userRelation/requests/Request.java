package Xeva.productiveApp.userRelation.requests;
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

}