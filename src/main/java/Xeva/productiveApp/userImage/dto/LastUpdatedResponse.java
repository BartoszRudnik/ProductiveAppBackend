package Xeva.productiveApp.userImage.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LastUpdatedResponse {

    LocalDateTime lastUpdated;

}
