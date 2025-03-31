package tn.cloudnine.queute.dto.workspace.projections;

import java.time.LocalDateTime;

public interface WorkspaceProjection {
    Long getWorkspaceId();
    String getName();
    String getDescription();
    String getImage();
    boolean getIsLocked();
    LocalDateTime getCreatedAt();
}
