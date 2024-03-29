package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeLocaleRequest {

    private String locale;
    private LocalDateTime lastUpdated;

}
