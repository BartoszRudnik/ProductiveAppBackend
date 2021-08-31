package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeTagsRequest {

    private Long id;
    private String name;
    private LocalDateTime lastUpdated;

}
