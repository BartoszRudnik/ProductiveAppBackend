package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeTagsRequestList {

    List<SynchronizeTagsRequest> tagList;

}
