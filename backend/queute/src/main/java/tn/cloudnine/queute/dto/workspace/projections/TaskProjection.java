package tn.cloudnine.queute.dto.workspace.projections;

import tn.cloudnine.queute.enums.workspace.TaskStatus;
import tn.cloudnine.queute.model.workspace.ProjectDocument;

import java.time.LocalDateTime;
import java.util.Set;

public interface TaskProjection {
    Long getTaskId();

    String getTitle();

    String getDescription();

    Integer getPriority();

    LocalDateTime getBeginDate();

    LocalDateTime getDeadline();

    Set<ProjectDocument> getDocuments();

    TaskStatus getStatus();
}
