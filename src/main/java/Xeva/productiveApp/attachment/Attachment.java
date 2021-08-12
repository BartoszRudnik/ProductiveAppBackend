package Xeva.productiveApp.attachment;

import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.task.Task;
import lombok.*;
import org.hibernate.annotations.Type;
import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Attachment")
public class Attachment {

    @Id
    @SequenceGenerator(
            name = "attachment_sequence",
            sequenceName = "attachment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "attachment_sequence"
    )
    private Long attachmentId;

    @Lob
    private byte[] file;

    @ManyToOne
    @JoinColumn(nullable = false, name = "task_id")
    private Task task;

    @ManyToOne
            @JoinColumn(nullable = false, name = "application_user_id")
            private ApplicationUser applicationUser;

    String fileName;

    Attachment(byte[] file, Task task, ApplicationUser applicationUser, String fileName){
        this.file = file;
        this.task = task;
        this.applicationUser = applicationUser;
        this.fileName = fileName;
    }

}
