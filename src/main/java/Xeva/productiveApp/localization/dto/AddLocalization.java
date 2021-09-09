package Xeva.productiveApp.localization.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddLocalization {

    private String localizationName;
    private String street;
    private String locality;
    private String country;
    private Float longitude;
    private Float latitude;
    private String uuid;

}
