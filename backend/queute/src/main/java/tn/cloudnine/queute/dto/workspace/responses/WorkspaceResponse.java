package tn.cloudnine.queute.dto.workspace.responses;

import tn.cloudnine.queute.dto.workspace.views.WorkspaceProjection;

public record WorkspaceResponse(
        WorkspaceProjection projection,
        ProjectResponse projectResponse
) {}
