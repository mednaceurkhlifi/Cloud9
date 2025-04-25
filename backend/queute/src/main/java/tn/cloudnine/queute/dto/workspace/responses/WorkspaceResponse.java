package tn.cloudnine.queute.dto.workspace.responses;

import tn.cloudnine.queute.dto.workspace.projections.WorkspaceProjection;

public record WorkspaceResponse(
        WorkspaceProjection projection,
        ProjectResponse projectResponse
) {}
