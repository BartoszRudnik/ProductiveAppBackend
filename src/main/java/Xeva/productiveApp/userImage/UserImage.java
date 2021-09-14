package Xeva.productiveApp.userImage;

import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="UserImage")
public class UserImage {

    @Id
    @SequenceGenerator(
            name = "user_image_sequence",
            sequenceName = "user_image_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_image_sequence"
    )
    private Long userImageId;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] image;

    private LocalDateTime lastUpdated = LocalDateTime.now();

    @OneToOne
    @JoinColumn(nullable = false, name = "application_user_id")
    private ApplicationUser user;

    UserImage(byte[] image, ApplicationUser user){
        this.image = image;
        this.user = user;
    }

}
