package Xeva.productiveApp.task;

import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Entity(name="Task")

public class Task {
    @Id
    @SequenceGenerator(
            name = "task_sequence",
            sequenceName = "task_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "task_sequence"
    )
    private Long id_task;
    @Column(
            name= "task_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String task_name;
    @Column(
            name= "description",
            columnDefinition = "TEXT"
    )
    private String description;
    @Column(
            name= "localization",
            nullable = false
    )
    @ManyToOne
    private ApplicationUser user;
    @Enumerated(EnumType.STRING)
    private TaskLocalization localization = TaskLocalization.INBOX;
    @Column(
            name= "priority",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.NORMAL;
    @Column(
            name= "ifDone",
            nullable = false
    )
    private Boolean ifDone = false;

    public Task(String task_name, String description, ApplicationUser user) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
    }

    public Task(String task_name, String description, ApplicationUser user, TaskLocalization localization, TaskPriority priority) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.localization = localization;
        this.priority = priority;
    }
}
