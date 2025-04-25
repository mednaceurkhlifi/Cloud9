package tn.cloudnine.queute.dto.workspace.projections;

import tn.cloudnine.queute.enums.workspace.ProjectStatus;
import tn.cloudnine.queute.model.workspace.ProjectDocument;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ProjectProjection {
    Long getProjectId();
    String getName();
    String getDescription();
    String getImage();
    Integer getPriority();
    Float getAchievement();
    LocalDateTime getBeginDate();
    LocalDateTime getDeadline();
    ProjectStatus getStatus();
    Set<ProjectDocument> getDocuments();
}
