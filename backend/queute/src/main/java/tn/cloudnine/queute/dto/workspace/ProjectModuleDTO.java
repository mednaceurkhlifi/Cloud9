package tn.cloudnine.queute.dto.workspace;

import tn.cloudnine.queute.model.workspace.ProjectDocument;

import java.time.LocalDateTime;
import java.util.Set;

public record ProjectModuleDTO(
        Long moduleIs,
        String title,
        String description,
        Integer priority,
        LocalDateTime beginDate,
        LocalDateTime deadline,
        Set<ProjectDocument> documents
) {
}
