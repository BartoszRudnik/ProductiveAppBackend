package Xeva.productiveApp.tags;

import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.task.Task;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Tag {

    @Id
    @SequenceGenerator(
            name = "tag_sequence",
            sequenceName = "tag_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tag_sequence"
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(
            name = "taskId",
            nullable = false
    )
    private Long taskId;

    public Tag(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Tag(Long id, String name, Long taskId){
        this.id = id;
        this.name = name;
        this.taskId = taskId;
    }

    public Tag(String name, Long taskId){
        this.name = name;
        this.taskId = taskId;
    }

}
