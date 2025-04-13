package tn.cloudnine.queute.dto.workspace.automation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tn.cloudnine.queute.enums.workspace.ProjectStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectRequest {
    private String name;
    private String description;
    private Integer priority;
    private Long workspace_id;
    private Float achievement;
    private LocalDateTime beginDate;
    private LocalDateTime deadline;
    private ProjectStatus status;
    private List<ModuleRequest> modules;
}
