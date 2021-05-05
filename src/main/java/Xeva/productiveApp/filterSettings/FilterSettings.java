package Xeva.productiveApp.filterSettings;

import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="FilterSettings")
public class FilterSettings {

    @Id
    @SequenceGenerator(
            name = "filter_sequence",
            sequenceName = "filter_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "filter_sequence"
    )
    private Long filterSettingsId;

    @OneToOne
    @JoinColumn(nullable = false, name = "application_user_id")
    private ApplicationUser user;

    private boolean showOnlyUnfinished;
    private boolean showOnlyDelegated;

    public FilterSettings(ApplicationUser user, boolean showOnlyUnfinished, boolean showOnlyDelegated){
        this.user = user;
        this.showOnlyUnfinished = showOnlyUnfinished;
        this.showOnlyDelegated = showOnlyDelegated;
    }

}
