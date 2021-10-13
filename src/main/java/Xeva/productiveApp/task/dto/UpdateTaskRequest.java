package Xeva.productiveApp.task.dto;

import Xeva.productiveApp.tags.Tag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateTaskRequest {

    private String uuid;
    private String taskName;
    private String taskDescription;
    private String userEmail;
    private String priority;
    private String localization;
    private String delegatedEmail;
    private boolean ifDone;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Tag> tags;
    private Double position;
    private boolean canceled;
    private String taskState;
    private String localizationUuid;
    private double localizationRadius;
    private boolean notificationOnEnter;
    private boolean notificationOnExit;

}