package Xeva.productiveApp.locale;

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
@Entity(name="Locale")
public class Locale {

    @Id
    @SequenceGenerator(
            name = "locale_sequence",
            sequenceName = "locale_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "locale_sequence"
    )
    private Long id;

    private String languageCode;

    @OneToMany(mappedBy = "locale")
    private List<ApplicationUser> applicationUsers;

    public Locale(String languageCode){
        this.languageCode = languageCode;
    }

    void addUser(ApplicationUser applicationUser){

        if(this.applicationUsers == null){
            this.applicationUsers = new ArrayList<>();
        }

        this.applicationUsers.add(applicationUser);

        applicationUser.setLocale(this);

    }

}
