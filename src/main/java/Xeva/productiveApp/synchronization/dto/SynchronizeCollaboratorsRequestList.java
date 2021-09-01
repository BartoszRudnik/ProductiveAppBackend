package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeCollaboratorsRequestList {

    private List<SynchronizeCollaboratorsRequest> collaboratorList;

}
