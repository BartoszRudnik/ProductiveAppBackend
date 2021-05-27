package Xeva.productiveApp.task;

import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.localization.Localization;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Task parentTask;

    @JoinColumn(
            name = "childTask"
    )
    @OneToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JsonIgnore
    private Task childTask;

    private Boolean isDelegated;

    private String taskStatus;

    private String delegatedEmail;

    private Boolean isCanceled;

    @JoinColumn(
            name = "notification_localization"
    )
    @ManyToOne
    private Localization notificationLocalization;

    private double localizationRadius;

    private Boolean notificationOnEnter;

    private Boolean notificationOnExit;

    //Tworzenie tasku nadrzędnego
    public Task(String task_name, String description, ApplicationUser user, TaskLocalization localization, TaskPriority priority, Boolean ifDone, Date startDate, Date endDate, ApplicationUser userDelegated, String delegatedEmail) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.localization = localization;
        this.priority = priority;
        this.ifDone = ifDone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.delegatedEmail = delegatedEmail;
        this.taskStatus = "Sent";
        this.childTask = new Task(task_name, description, userDelegated, priority, ifDone, startDate, endDate, this);
        this.isCanceled = false;
    }

    //Tworzenie tasku podrzędnego
    public Task( String task_name, String description, ApplicationUser user, TaskPriority priority, Boolean ifDone, Date startDate, Date endDate, Task parentTask) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.priority = priority;
        this.ifDone = ifDone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.parentTask = parentTask;
        this.isDelegated = true;
        this.isCanceled = false;
    }

    //Tworzenie tasku nadrzędnego z powiadomieniem
    public Task(String task_name, String description, ApplicationUser user, TaskLocalization localization, TaskPriority priority, Boolean ifDone, Date startDate, Date endDate, ApplicationUser userDelegated, String delegatedEmail, Localization notificationLocalization, double localizationRadius, boolean notificationOnEnter, boolean notificationOnExit) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.localization = localization;
        this.priority = priority;
        this.ifDone = ifDone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.delegatedEmail = delegatedEmail;
        this.taskStatus = "Sent";
        this.notificationLocalization = notificationLocalization;
        this.notificationOnEnter = notificationOnEnter;
        this.notificationOnExit = notificationOnExit;
        this.localizationRadius = localizationRadius;
        this.childTask = new Task(task_name, description, userDelegated, priority, ifDone, startDate, endDate, this, notificationLocalization, localizationRadius, notificationOnEnter, notificationOnExit);
        this.isCanceled = false;
    }

    //Tworzenie tasku podrzędnego z powiadomieniem
    public Task( String task_name, String description, ApplicationUser user, TaskPriority priority, Boolean ifDone, Date startDate, Date endDate, Task parentTask, Localization notificationLocalization, double localizationRadius, boolean notificationOnEnter, boolean notificationOnExit) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.priority = priority;
        this.ifDone = ifDone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.parentTask = parentTask;
        this.isDelegated = true;
        this.isCanceled = false;
        this.localizationRadius = localizationRadius;
        this.notificationLocalization = notificationLocalization;
        this.notificationOnEnter = notificationOnEnter;
        this.notificationOnExit = notificationOnExit;
    }

    public Task(String task_name, String description, ApplicationUser user) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
    }

    public Task(String task_name, String description, ApplicationUser user, Date startDate, Date endDate, boolean ifDone, TaskPriority priority, TaskLocalization localization, String delegatedEmail) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ifDone = ifDone;
        this.priority = priority;
        this.localization = localization;
        this.delegatedEmail = delegatedEmail;
        this.taskStatus = "Sent";
    }

    public Task(String task_name, String description, ApplicationUser user, Date startDate, Date endDate, boolean ifDone, TaskPriority priority, TaskLocalization localization, String delegatedEmail, Localization notificationLocalization, boolean notificationOnEnter, double localizationRadius) {
        this.task_name = task_name;
        this.description = description;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ifDone = ifDone;
        this.priority = priority;
        this.localization = localization;
        this.delegatedEmail = delegatedEmail;
        this.taskStatus = "Sent";
        this.notificationLocalization = notificationLocalization;
        this.localizationRadius = localizationRadius;
        this.notificationOnEnter = notificationOnEnter;
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
