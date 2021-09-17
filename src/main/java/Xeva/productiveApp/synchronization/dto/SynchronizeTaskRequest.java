package Xeva.productiveApp.synchronization.dto;

import Xeva.productiveApp.tags.Tag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeTaskRequest {

    private Long id;
    private double position;
    private String title;
    private String description;
    private String priority;
    private String localization;
    private String delegatedEmail;
    private String taskStatus;
    private String supervisorEmail;
    private String uuid;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String tags;
    private boolean done;
    private boolean isDelegated;
    private boolean isCanceled;
    private int parentId;
    private int childId;
    private String notificationLocalizationUuid;
    private double notificationLocalizationRadius;
    private boolean notificationOnEnter;
    private boolean notificationOnExit;
    private LocalDateTime lastUpdated;

}
