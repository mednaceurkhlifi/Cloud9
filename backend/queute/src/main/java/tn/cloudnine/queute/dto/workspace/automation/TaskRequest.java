package tn.cloudnine.queute.dto.workspace.automation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tn.cloudnine.queute.enums.workspace.TaskStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TaskRequest {
    private String title;
    private String description;
    private Integer priority;
    private LocalDateTime beginDate;
    private LocalDateTime deadline;
    private TaskStatus status;
}
