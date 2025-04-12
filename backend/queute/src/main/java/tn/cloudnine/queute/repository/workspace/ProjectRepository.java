package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.dto.workspace.projections.ProjectModuleProjection;
import tn.cloudnine.queute.dto.workspace.projections.ProjectProjection;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.model.workspace.ProjectDocument;
import tn.cloudnine.queute.model.workspace.ProjectModule;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<ProjectProjection> findByWorkspaceWorkspaceId(Long workspaceId, Pageable pageable);

    List<Project> findAllByDocumentsContains(ProjectDocument document);
}
