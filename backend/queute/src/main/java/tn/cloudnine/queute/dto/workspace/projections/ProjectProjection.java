package tn.cloudnine.queute.dto.workspace.projections;

import tn.cloudnine.queute.enums.workspace.ProjectStatus;
import tn.cloudnine.queute.model.workspace.ProjectDocument;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectProjection {
    Long getProjectId();
    String getName();
    String getDescription();
    String getImage();
    Integer getPriority();
    LocalDateTime getBeginDate();
    LocalDateTime getDeadline();
    ProjectStatus getStatus();
    List<ProjectDocument> getDocuments();
}
