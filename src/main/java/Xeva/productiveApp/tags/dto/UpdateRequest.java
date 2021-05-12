package Xeva.productiveApp.tags.dto;

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
