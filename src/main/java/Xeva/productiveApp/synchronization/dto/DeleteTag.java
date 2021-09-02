package Xeva.productiveApp.synchronization.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class DeleteTag {

    String ownerEmail;
    String tagName;

}
