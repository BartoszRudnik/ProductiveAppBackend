package Xeva.productiveApp.task.dto;

import Xeva.productiveApp.tags.Tag;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddTaskRequest {

    private String taskName;
    private String taskDescription;
    private String userEmail;
    private String priority;
    private String localization;
    private String delegatedEmail;
    private boolean ifDone;
    private Date startDate;
    private Date endDate;
    private List<Tag> tags;

    private Long localizationId;
    private double localizationRadius;
    private boolean notificationOnEnter;

}
