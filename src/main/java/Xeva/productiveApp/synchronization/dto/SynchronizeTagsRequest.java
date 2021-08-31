package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.util.Date;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeTagsRequest {

    private Long id;
    private String name;
    private Date lastUpdated;

}
