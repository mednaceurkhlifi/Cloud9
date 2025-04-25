package tn.cloudnine.queute.dto.workspace.automation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ModuleRequest {
    private String title;
    private String description;
    private Integer priority;
    private Float achievement;
    private LocalDateTime beginDate;
    private LocalDateTime deadline;
    private List<TaskRequest> tasks;
}
