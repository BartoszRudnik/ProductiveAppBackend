package Xeva.productiveApp.tags;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UpdateRequest {

    String oldName;
    String newName;

}
