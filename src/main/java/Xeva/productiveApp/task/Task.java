package Xeva.productiveApp.task;

import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.attachment.Attachment;
import Xeva.productiveApp.localization.Localization;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Task")
public class Task {
    /*
    Dane zadań:
    - id Long
    - taskName String
    - description String
    - ApplicationUser User
    - list TaskList - lista na której zadanie się znajduje
    - priority TaskPriority
    - ifDone Boolean
    - startDate Date
    - endDate Date
    - position Double
    - parrentTask - zadanie nadrzędne
    - childTask - zadanie podrzędne
    - isDelegated - czy zostało zlecone
    - taskStatus - status zadania
    - delegatedEmail - email osoby delegującej
    - isCanceled - czy zostało anulowane (z góry)
    - notificationLocalization - lokalizacja przypisana do zadania
    - localizationRadius - zakres przypominania (średnica okręgu)
    - notificationOnEnter - czy przypomnieć na wejściu w okrąg?
    - notificationOnExit - czy przypomnieć na wyjściu z okręgu?
    - lastUpdated - data ostatniej zmiany*/
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
    private Long id;

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String taskName;
    @Column(
            columnDefinition = "TEXT"
    )
    private String description;

    @ManyToOne
    @JoinColumn(name = "application_user_id")
    private ApplicationUser user;

    @Enumerated(EnumType.STRING)
    private TaskList taskList = TaskList.INBOX;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.NORMAL;

    private Boolean ifDone = false;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private double position;

    @OneToOne
    @JsonIgnore
    private Task parentTask;

    @JoinColumn(
            name = "childTask"
    )
    @OneToOne(cascade = {
            CascadeType.PERSIST,
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

    private LocalDateTime lastUpdated;

    @Column(nullable = false)
    private String uuid;

    private String taskState = "COLLECT";

    @JsonIgnore
    @OneToMany(mappedBy = "task")
    private List<Attachment> attachments;

    public void addAttachment(Attachment attachment){
        if(this.attachments == null){
            this.attachments = new ArrayList<>();
        }

        this.attachments.add(attachment);

        attachment.setTask(this);
    }

    @PrePersist
    public void onInsert() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    //Tworzenie tasku nadrzędnego
    public Task(String taskName, String description, ApplicationUser user, TaskList taskList, TaskPriority priority, Boolean ifDone, LocalDateTime startDate, LocalDateTime endDate, ApplicationUser userDelegated, String delegatedEmail, String uuid, String taskState) {
        this.taskName = taskName;
        this.description = description;
        this.user = user;
        this.taskList = taskList;
        this.priority = priority;
        this.ifDone = ifDone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.delegatedEmail = delegatedEmail;
        this.taskStatus = "Sent";
        this.childTask = new Task(taskName, description, userDelegated, priority, ifDone, startDate, endDate, this, UUID.randomUUID().toString());
        this.isCanceled = false;
        this.uuid = uuid;
        this.taskState = taskState;
    }

    //Tworzenie tasku podrzędnego
    public Task(String taskName, String description, ApplicationUser user, TaskPriority priority, Boolean ifDone, LocalDateTime startDate, LocalDateTime endDate, Task parentTask, String uuid) {
        this.taskName = taskName;
        this.description = description;
        this.user = user;
        this.priority = priority;
        this.ifDone = ifDone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.parentTask = parentTask;
        this.isDelegated = true;
        this.isCanceled = false;
        this.uuid = uuid;
    }

    //Tworzenie tasku nadrzędnego z powiadomieniem
    public Task(String taskName, String description, ApplicationUser user, TaskList taskList, TaskPriority priority, Boolean ifDone, LocalDateTime startDate, LocalDateTime endDate, ApplicationUser userDelegated, String delegatedEmail, Localization notificationLocalization, double localizationRadius, boolean notificationOnEnter,
                boolean notificationOnExit, String uuid, String taskState) {
        this.taskName = taskName;
        this.description = description;
        this.user = user;
        this.taskList = taskList;
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
        this.childTask = new Task(taskName, description, userDelegated, priority, ifDone, startDate, endDate, this, notificationLocalization, localizationRadius, notificationOnEnter, notificationOnExit, UUID.randomUUID().toString());
        this.isCanceled = false;
        this.uuid = uuid;
        this.taskState = taskState;
    }

    //Tworzenie tasku podrzędnego z powiadomieniem
    public Task(String taskName, String description, ApplicationUser user, TaskPriority priority, Boolean ifDone, LocalDateTime startDate, LocalDateTime endDate, Task parentTask, Localization notificationLocalization, double localizationRadius, boolean notificationOnEnter, boolean notificationOnExit, String uuid) {
        this.taskName = taskName;
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
        this.uuid = uuid;
    }


    public Task(String taskName, String description, ApplicationUser user, LocalDateTime startDate, LocalDateTime endDate, boolean ifDone, TaskPriority priority, TaskList taskList, String delegatedEmail, String uuid, String taskState) {
        this.taskName = taskName;
        this.description = description;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ifDone = ifDone;
        this.priority = priority;
        this.taskList = taskList;
        this.delegatedEmail = delegatedEmail;
        this.taskStatus = "Sent";
        this.uuid = uuid;
        this.taskState = taskState;
    }

    public Task(String taskName, String description, ApplicationUser user, LocalDateTime startDate, LocalDateTime endDate, boolean ifDone, TaskPriority priority, TaskList taskList, String delegatedEmail, Localization notificationLocalization, boolean notificationOnEnter, double localizationRadius, boolean notificationOnExit, String uuid, String taskState) {
        this.taskName = taskName;
        this.description = description;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ifDone = ifDone;
        this.priority = priority;
        this.taskList = taskList;
        this.delegatedEmail = delegatedEmail;
        this.taskStatus = "Sent";
        this.notificationLocalization = notificationLocalization;
        this.localizationRadius = localizationRadius;
        this.notificationOnEnter = notificationOnEnter;
        this.notificationOnExit = notificationOnExit;
        this.uuid = uuid;
        this.taskState = taskState;
    }
}
