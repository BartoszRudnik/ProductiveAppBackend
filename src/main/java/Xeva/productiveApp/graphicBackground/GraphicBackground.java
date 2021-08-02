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
    @OneToOne
    @JoinColumn(nullable = false, name = "application_user_id")
    private ApplicationUser user;
    @Enumerated(EnumType.STRING)
    private BackgroundType backgroundType;
}
