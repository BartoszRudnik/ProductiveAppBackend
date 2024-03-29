package Xeva.productiveApp.task.dto;

import Xeva.productiveApp.localization.Localization;
import Xeva.productiveApp.tags.Tag;
import Xeva.productiveApp.task.Task;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetTasksResponse {

    private Task tasks;
    private List<Tag> tags;
    private String supervisorEmail;
    private String childUuid;
    private String parentUuid;

    public GetTasksResponse(Task task, List<Tag> tags){
        this.tasks = task;
        this.tags = tags;
    }

}
