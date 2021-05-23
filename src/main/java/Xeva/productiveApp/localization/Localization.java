package Xeva.productiveApp.localization;

import Xeva.productiveApp.appUser.ApplicationUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Localization")
public class Localization {

    @Id
    @SequenceGenerator(
            name = "localization_sequence",
            sequenceName = "localization_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "localization_sequence"
    )
    private Long localizationId;

    private String localizationName;
    private Float longitude;
    private Float latitude;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false, name = "application_user_id")
    private ApplicationUser user;

    public Localization(String localizationName, Float longitude, Float latitude, ApplicationUser user){
        this.localizationName = localizationName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.user = user;
    }

}
