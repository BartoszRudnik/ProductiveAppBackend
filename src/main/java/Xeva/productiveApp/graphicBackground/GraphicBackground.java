package Xeva.productiveApp.graphicBackground;

import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="GraphicBackground")
public class GraphicBackground {

    @Id
    @SequenceGenerator(
            name = "graphicBackground_sequence",
            sequenceName = "graphicBackground_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "graphicBackground_sequence"
    )
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false, name = "application_user_id")
    private ApplicationUser user;

    @Enumerated(EnumType.STRING)
    private BackgroundType backgroundType;

    public GraphicBackground(ApplicationUser applicationUser, BackgroundType backgroundType){

        this.user = applicationUser;
        this.backgroundType = backgroundType;

    }

}
