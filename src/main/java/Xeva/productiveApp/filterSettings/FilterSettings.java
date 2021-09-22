package Xeva.productiveApp.filterSettings;

import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false, name = "application_user_id", unique = true)
    private ApplicationUser user;

    private boolean showDelegated;
    private boolean showWithLocation;

    @ElementCollection
    private List<String> collaboratorEmail;

    @ElementCollection
    private List<String> locations;

    @ElementCollection
    private List<String> priorities;

    @ElementCollection
    private List<String> tags;

    private int sortingMode;

    private LocalDateTime lastUpdated;

    @PrePersist
    public void onInsert() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    public FilterSettings(ApplicationUser user, boolean showDelegated, boolean showWithLocation){
        this.user = user;
        this.showDelegated = showDelegated;
        this.showWithLocation = showWithLocation;
        this.collaboratorEmail = new ArrayList<>();
        this.priorities = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.sortingMode = 0;
    }

    public FilterSettings(ApplicationUser user, boolean showDelegated, boolean showWithLocation, List<String> collaboratorEmail, List<String> priorities, List<String> tags, List<String> locations, int sortingMode){
        this.user = user;
        this.showDelegated = showDelegated;
        this.showWithLocation = showWithLocation;
        this.collaboratorEmail = collaboratorEmail;
        this.priorities = priorities;
        this.tags = tags;
        this.locations = locations;
        this.sortingMode = sortingMode;
    }

}
