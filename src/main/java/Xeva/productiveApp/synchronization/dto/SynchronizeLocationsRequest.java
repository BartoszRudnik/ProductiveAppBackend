package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeLocationsRequest {

    private Long id;
    private String localizationName;
    private String locality;
    private String street;
    private String country;
    private Float longitude;
    private Float latitude;
    private LocalDateTime lastUpdated;
    private String uuid;
    private Boolean saved;

}
