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

    @OneToMany(mappedBy = "graphicBackground")
    private List<ApplicationUser> users;

    @Enumerated(EnumType.STRING)
    private BackgroundType backgroundType;

    public GraphicBackground(BackgroundType backgroundType){

        this.backgroundType = backgroundType;

    }

    public void addUser(ApplicationUser newUser){
        if(this.users == null){
            this.users = new ArrayList<>();
        }

        this.users.add(newUser);

        newUser.setGraphicBackground(this);

    }

}
