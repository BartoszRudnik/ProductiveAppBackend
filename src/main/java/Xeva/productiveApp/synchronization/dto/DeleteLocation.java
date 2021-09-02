package Xeva.productiveApp.synchronization.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeleteLocation {

    String ownerEmail;
    String locationName;

}
