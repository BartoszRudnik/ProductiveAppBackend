package Xeva.productiveApp.task.requests;

import Xeva.productiveApp.tags.Tag;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateTaskPositionRequest {

    private double position;

}