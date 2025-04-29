package tn.cloudnine.queute.dto.workspace.projections;

import tn.cloudnine.queute.enums.workspace.ProjectRole;

public interface ProjectUserProjection {
        ProjectRole getRole();
        ProjectProjection getProject();
        UserProjection getUser();
}
