package Xeva.productiveApp.filterSettings.pojo;

import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TagsRequest {

    private List<String> tags;

}
