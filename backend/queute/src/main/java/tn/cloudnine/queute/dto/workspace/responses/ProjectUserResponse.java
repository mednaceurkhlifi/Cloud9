package tn.cloudnine.queute.dto.workspace.responses;

import tn.cloudnine.queute.dto.workspace.ProjectUserDTO;
import tn.cloudnine.queute.dto.workspace.projections.ProjectUserProjection;

import java.util.Set;

public record ProjectUserResponse(
//        Set<ProjectUserDTO> projectUser,
        Set<ProjectUserProjection> projections,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
) {
}
