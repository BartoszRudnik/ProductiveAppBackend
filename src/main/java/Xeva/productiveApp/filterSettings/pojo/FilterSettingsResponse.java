package Xeva.productiveApp.filterSettings.pojo;

import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilterSettingsResponse {

    private boolean showOnlyUnfinished;
    private boolean showOnlyDelegated;
    private List<String> collaboratorEmail;
    private List<String> priorities;
    private List<String> tags;
    private int sortingMode;

}
