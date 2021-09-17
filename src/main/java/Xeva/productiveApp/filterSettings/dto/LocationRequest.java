package Xeva.productiveApp.filterSettings.dto;

import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationRequest {

    private List<String> locations;

}
