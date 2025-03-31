package tn.cloudnine.queute.dto.workspace.projections;

import tn.cloudnine.queute.enums.workspace.ProjectStatus;

import java.time.LocalDateTime;

public interface ProjectProjection {
    Long getProjectId();
    String getName();
    String getDescription();
    String getImage();
    Integer getPriority();
    LocalDateTime getBeginDate();
    LocalDateTime getDeadline();
    ProjectStatus getStatus();
}
