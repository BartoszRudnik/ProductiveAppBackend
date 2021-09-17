package Xeva.productiveApp.filterSettings.dto;

import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilterSettingsResponse {

    private Long id;
    private boolean showOnlyUnfinished;
    private boolean showOnlyDelegated;
    private boolean showOnlyWithLocalization;
    private List<String> collaboratorEmail;
    private List<String> priorities;
    private List<String> tags;
    private List<String> locations;
    private int sortingMode;

}
