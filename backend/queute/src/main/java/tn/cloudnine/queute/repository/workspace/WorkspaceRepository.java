package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.dto.workspace.projections.WorkspaceProjection;
import tn.cloudnine.queute.model.workspace.Workspace;

import java.util.Optional;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Optional<WorkspaceProjection> findByWorkspaceIdAndIsDeletedIsFalse(Long workspace_id);
}
