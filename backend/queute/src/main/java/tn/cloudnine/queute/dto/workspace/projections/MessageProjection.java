package tn.cloudnine.queute.dto.workspace.projections;

import tn.cloudnine.queute.model.workspace.ProjectDocument;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageProjection {
    ProjectUserProjection getUser();
    String getMessage();
    List<ProjectDocument> getAttachments();
    LocalDateTime getCreatedAt();
}
