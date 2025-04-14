package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.dto.workspace.projections.MessageProjection;
import tn.cloudnine.queute.model.workspace.WorkspaceMessage;

@Repository
public interface WorkspaceMessageRepository extends JpaRepository<WorkspaceMessage, Long> {
    Page<MessageProjection> findByWorkspaceWorkspaceId(Long workspaceId, Pageable pageable);

    Page<MessageProjection> findByModuleModuleId(Long moduleId, Pageable pageable);

    Page<MessageProjection> findByProjectProjectId(Long projectId, Pageable pageable);

    Page<MessageProjection> findByTaskTaskId(Long taskId, Pageable pageable);
}
