package Xeva.productiveApp.task;

import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(nullable = false, name = "application_user_id")
    private ApplicationUser user;

    @Column(
            name= "localization",
            nullable = false
    )
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

    @Column(
            name = "start_date",
            nullable = false
    )
    private Date startDate;

    @Column(
            name = "end_date",
            nullable = false
    )
    private Date endDate;

    @Column(
            name = "position"
    )
    private double position;

    @JoinColumn(
            name = "parentTask"
    )
    @OneToOne
    private Task parentTask;

    @JoinColumn(
            name = "childTask"
    )
    @OneToOne
    private Task childTask;

    @Column(
            name= "ifUnfastened",
            nullable = false
    )
    private Boolean ifUnfastened = false;

    @Column(
            name= "ifUnused",
            nullable = false
    )
    private Boolean ifUnused = false;

    //Tworzenie tasku nadrzędnego
    public Task(String task_name, String description, ApplicationUser user, TaskLocalization localization, TaskPriority priority, Boolean ifDone, Date startDate, Date endDate, double position, ApplicationUser userDelegated) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.localization = localization;
        this.priority = priority;
        this.ifDone = ifDone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.childTask = new Task(task_name, description, userDelegated, localization, priority, ifDone, startDate, endDate, position, this);
    }

    //Tworzenie tasku podrzędnego
    public Task( String task_name, String description, ApplicationUser user, TaskLocalization localization, TaskPriority priority, Boolean ifDone, Date startDate, Date endDate, double position, Task parentTask) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.localization = localization;
        this.priority = priority;
        this.ifDone = ifDone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.parentTask = parentTask;
        this.ifUnused = true;
    }

    public Task(String task_name, String description, ApplicationUser user) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
    }

    public Task(String task_name, String description, ApplicationUser user, Date startDate, Date endDate, boolean ifDone, TaskPriority priority, TaskLocalization localization) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ifDone = ifDone;
        this.priority = priority;
        this.localization = localization;
    }

    public Task(String task_name, String description, ApplicationUser user, Date startDate, Date endDate, boolean ifDone, TaskPriority priority, TaskLocalization localization, double position) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ifDone = ifDone;
        this.priority = priority;
        this.localization = localization;
        this.position = position;
    }

    public Task(String task_name, String description, ApplicationUser user, TaskLocalization localization, TaskPriority priority, Date startDate, Date endDate) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.localization = localization;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
