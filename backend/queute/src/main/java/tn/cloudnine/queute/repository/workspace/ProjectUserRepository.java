package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.dto.workspace.projections.ProjectUserProjection;
import tn.cloudnine.queute.enums.workspace.ProjectRole;
import tn.cloudnine.queute.model.workspace.ProjectUser;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    long countByProjectProjectIdAndRole(Long projectId, ProjectRole role);
    Page<ProjectUserProjection> findByUserUserId(Long id, Pageable pageable);
}
