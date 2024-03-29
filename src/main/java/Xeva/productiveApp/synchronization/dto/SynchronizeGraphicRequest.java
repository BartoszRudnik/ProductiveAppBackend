package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class SynchronizeGraphicRequest {

    private String mode;
    private LocalDateTime lastUpdated;

}
