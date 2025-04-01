package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.dto.workspace.projections.ProjectProjection;
import tn.cloudnine.queute.model.workspace.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<ProjectProjection> findByWorkspaceId(Long workspaceId, Pageable pageable);
}
