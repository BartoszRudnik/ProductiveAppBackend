package Xeva.productiveApp.task;

import lombok.*;

import java.util.Date;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddTaskRequest {

    private String taskName;
    private String taskDescription;
    private String userEmail;
    private boolean ifDone;
    private Date startDate;
    private Date endDate;

}
