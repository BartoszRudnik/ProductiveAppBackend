package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeUserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String userType;
    private boolean removed;
    private LocalDateTime lastUpdatedImage;
    private LocalDateTime lastUpdatedName;
    private byte [] localImage;

}
