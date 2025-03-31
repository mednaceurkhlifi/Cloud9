package tn.cloudnine.queute.dto.workspace.responses;

import tn.cloudnine.queute.dto.workspace.views.ProjectProjection;

import java.util.List;

public record ProjectResponse(
        List<ProjectProjection> projection,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
) {}
