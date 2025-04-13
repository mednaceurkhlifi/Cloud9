package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.dto.workspace.projections.ProjectModuleProjection;
import tn.cloudnine.queute.dto.workspace.projections.TaskProjection;
import tn.cloudnine.queute.model.workspace.ProjectDocument;
import tn.cloudnine.queute.model.workspace.ProjectModule;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ModuleRepository extends JpaRepository<ProjectModule, Long> {

    Optional<ProjectModuleProjection> findByModuleId(Long module_id);
    Page<ProjectModule> findByProjectProjectId(Long project_id, Pageable pageable);
    Set<ProjectModule> findByProjectProjectId(Long project_id);
    @Query("SELECT pm.moduleId FROM ProjectModule pm WHERE pm.project.projectId = :projectId")
    Set<Long> findRelatedModuleIdByProject(@Param("projectId") Long projectId);

    List<ProjectModule> findAllByDocumentsContains(ProjectDocument document);
}
