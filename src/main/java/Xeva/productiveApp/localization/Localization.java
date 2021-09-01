package Xeva.productiveApp.localization;

import Xeva.productiveApp.appUser.ApplicationUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private Long id;

    private String localizationName;
    private String street;
    private String locality;
    private String country;
    private Float longitude;
    private Float latitude;

    private LocalDateTime lastUpdated;

    @PrePersist
    public void onInsert() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false, name = "application_user_id")
    private ApplicationUser user;

    public Localization(String localizationName, String street, String locality, String country, Float longitude, Float latitude, ApplicationUser user){
        this.localizationName = localizationName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.user = user;
        this.street = street;
        this.country = country;
        this.locality = locality;
    }

}
