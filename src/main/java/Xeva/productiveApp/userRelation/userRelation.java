package Xeva.productiveApp.userRelation;

import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="userRelation")
public class userRelation {
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
    private Long id_userRelation;

    @ManyToOne
    @JoinColumn(nullable = false, name = "application_user_id")
    private ApplicationUser user1;

    @ManyToOne
    @JoinColumn(nullable = false, name = "application_user_id")
    private ApplicationUser user2;

    @Column(
            name = "state",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private relationState state = relationState.WAITING;

    public userRelation(ApplicationUser user1, ApplicationUser user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public userRelation(ApplicationUser user1, ApplicationUser user2, relationState state) {
        this.user1 = user1;
        this.user2 = user2;
        this.state = state;
    }
}
