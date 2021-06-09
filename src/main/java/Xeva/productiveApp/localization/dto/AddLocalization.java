package Xeva.productiveApp.localization.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddLocalization {

    String localizationName;
    String street;
    String locality;
    String country;
    Float longitude;
    Float latitude;

}
