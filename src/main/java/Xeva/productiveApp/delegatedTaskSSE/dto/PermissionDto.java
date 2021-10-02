package Xeva.productiveApp.delegatedTaskSSE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {

    String relationUuid;
    String action;

}
