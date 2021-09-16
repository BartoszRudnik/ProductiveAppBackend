package Xeva.productiveApp.task.dto;

import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeleteAllRequest {

    List<String> tasks;

}
