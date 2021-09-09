package Xeva.productiveApp.synchronization.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SynchronizeSettingsRequest {

    private boolean showOnlyUnfinished;
    private boolean showOnlyDelegated;
    private boolean showOnlyWithLocalization;
    private List<String> collaborators;
    private List<String> priorities;
    private List<Integer> locations;
    private List<String> tags;
    private int sortingMode;
    private String taskName;
    private LocalDateTime lastUpdated;

}
