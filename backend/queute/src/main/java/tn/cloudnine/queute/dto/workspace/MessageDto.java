package tn.cloudnine.queute.dto.workspace;

import tn.cloudnine.queute.model.workspace.ProjectDocument;

import java.time.LocalDateTime;
import java.util.List;

public record MessageDto(
        UserDTO sender,
        String message,
        List<ProjectDocument> attachments,
        LocalDateTime createdAt
) {
}
