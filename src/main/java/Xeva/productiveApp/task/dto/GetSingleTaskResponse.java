package Xeva.productiveApp.task.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetSingleTaskResponse {

    private Long taskId;
    private String ownerEmail;
    private String taskName;
    private String description;
    private String priority;
    private Date startDate;
    private Date endDate;

}
