package Xeva.productiveApp.task.requests;

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

}
