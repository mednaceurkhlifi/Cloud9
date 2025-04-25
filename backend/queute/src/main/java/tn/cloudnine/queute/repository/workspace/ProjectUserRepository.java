package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.dto.workspace.projections.ProjectUserProjection;
import tn.cloudnine.queute.enums.workspace.ProjectRole;
import tn.cloudnine.queute.model.Embeddable.ProjectUserId;
import tn.cloudnine.queute.model.workspace.ProjectUser;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.Set;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    long countByProjectProjectIdAndRole(Long projectId, ProjectRole role);
    Page<ProjectUserProjection> findByUserUserId(Long id, Pageable pageable);
    Set<ProjectUserProjection> findByUserEmail(String email);
    Set<ProjectUserProjection> findByProjectProjectId(Long id);
    Optional<ProjectUser> findById(ProjectUserId id);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProjectUser pu WHERE pu.project.projectId = :projectId")
    void deleteRelatedProjectProjectUser(@Param("projectId") Long projectId);

}
