package tn.cloudnine.queute.dto.workspace.projections;

import tn.cloudnine.queute.model.workspace.ProjectDocument;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageProjection {
    UserProjection getSender();
    String getMessage();
    List<ProjectDocument> getAttachments();
    LocalDateTime getCreatedAt();
}
