package tn.cloudnine.queute.dto.workspace.responses;

import tn.cloudnine.queute.dto.workspace.projections.ProjectModuleProjection;
import tn.cloudnine.queute.model.workspace.ProjectModule;

import java.util.List;

public record ModuleResponse(
//        List<ProjectModuleProjection> projection,
        List<ProjectModule> modules,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
) {
}
