package tn.cloudnine.queute.dto.workspace;

import tn.cloudnine.queute.enums.workspace.ProjectStatus;

import java.time.LocalDateTime;

public record ProjectDTO(
        Long projectId,
        String name,
        String description,
        Integer priority,
        LocalDateTime beginDate,
        LocalDateTime deadline,
        ProjectStatus status
) {
}
