package Xeva.productiveApp.localization.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddLocalization {

    String localizationName;
    Float longitude;
    Float latitude;

}
