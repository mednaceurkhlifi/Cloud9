package tn.cloudnine.queute.dto.workspace.projections;

import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.model.workspace.ProjectDocument;
import tn.cloudnine.queute.model.workspace.Task;

import java.time.LocalDateTime;
import java.util.Set;

public interface ProjectModuleProjection {
    Long getModuleId();
    String getTitle();
    String getDescription();
    Integer getPriority();
    LocalDateTime getBeginDate();
    LocalDateTime getDeadline();
    Set<ProjectDocument> getDocuments();
    Float getAchievement();
    Project getProject();
}
