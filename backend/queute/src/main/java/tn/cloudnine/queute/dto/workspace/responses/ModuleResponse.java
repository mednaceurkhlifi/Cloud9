package tn.cloudnine.queute.dto.workspace.responses;

import tn.cloudnine.queute.dto.workspace.projections.ProjectModuleProjection;

import java.util.List;

public record ModuleResponse(
        List<ProjectModuleProjection> projection,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
) {
}
