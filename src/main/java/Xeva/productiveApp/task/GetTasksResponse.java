package Xeva.productiveApp.task;

import Xeva.productiveApp.tags.Tag;
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
