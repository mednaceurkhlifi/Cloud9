package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.dto.workspace.projections.TaskProjection;
import tn.cloudnine.queute.model.workspace.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<TaskProjection> findAllByModuleIdAndDeletedFalse(Long module_id, Pageable pageable);
    Page<TaskProjection> findAllByProjectIdAndDeletedFalse(Long project_id, Pageable pageable);
}
