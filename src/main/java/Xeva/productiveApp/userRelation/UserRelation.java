package Xeva.productiveApp.userRelation;

import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="userRelation")
public class UserRelation {
    @Id
    @SequenceGenerator(
            name = "userRelation_sequence",
            sequenceName = "userRelation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userRelation_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "application_user_id"
    )
    private ApplicationUser user1;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "collaborator_id"
    )
    private ApplicationUser user2;

    @Column(
            name = "state",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private RelationState state = RelationState.WAITING;

    private LocalDateTime lastUpdated;

    private boolean user1Permission = false;
    private boolean user2Permission = false;
    private boolean user1AskForPermission = false;
    private boolean user2AskForPermission = false;

    @PrePersist
    public void onInsert() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    public UserRelation(ApplicationUser user1, ApplicationUser user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public UserRelation(ApplicationUser user1, ApplicationUser user2, RelationState state) {
        this.user1 = user1;
        this.user2 = user2;
        this.state = state;
    }
}
