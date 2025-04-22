package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.dto.workspace.projections.TaskProjection;
import tn.cloudnine.queute.enums.workspace.TaskStatus;
import tn.cloudnine.queute.model.workspace.ProjectDocument;
import tn.cloudnine.queute.model.workspace.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<TaskProjection> findAllByModuleModuleId(Long module_id, Pageable pageable);
    Page<TaskProjection> findAllByProjectProjectId(Long project_id, Pageable pageable);
    @Query("SELECT t.taskId FROM Task t WHERE t.project.projectId = :projectId")
    Set<Long> findRelatedTaskIdByProject(@Param("projectId") Long projectId);

    @Query("SELECT t.taskId FROM Task t WHERE t.module.moduleId = :moduleId")
    Set<Long> findRelatedTaskIdByModule(@Param("moduleId") Long moduleId);
    @Query("SELECT t FROM Task t JOIN t.members m WHERE m.email = :email")
    Set<Task> findTasksByUserEmail(@Param("email") String email);

    @Query("SELECT t FROM Task t JOIN t.members m WHERE m.email = :email")
    Page<TaskProjection> findTasksByUserEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT t FROM Task t JOIN t.members m WHERE t.taskId = :taskId AND m.email = :email")
    Optional<Task> findTasksByIdAndUserEmail(@Param("taskId") Long taskId, @Param("email") String email);

    long countByModuleModuleIdAndStatus(Long moduleId, TaskStatus status);
    long countByModuleModuleIdAndStatusNot(Long moduleId, TaskStatus status);
    long countByProjectProjectIdAndStatus(Long projectId, TaskStatus status);
    long countByProjectProjectIdAndStatusNot(Long projectId, TaskStatus status);
    @Query("SELECT COUNT(t) FROM Task t WHERE t.module.moduleId IN :moduleIds AND t.status = :status")
    long countByModulesAndStatus(@Param("moduleIds") Set<Long> moduleIds, @Param("status") TaskStatus status);
    @Query("SELECT COUNT(t) FROM Task t WHERE t.module.moduleId IN :moduleIds AND t.status != :status")
    long countByModulesAndStatusNotEquals(@Param("moduleIds") Set<Long> moduleIds, @Param("status") TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.deadline >= :startOfDay AND t.deadline < :endOfDay AND t.status != :status")
    Set<Task> findTasksWithDeadline(@Param("startOfDay") LocalDateTime startOfDay,
                                             @Param("endOfDay") LocalDateTime endOfDay, @Param("status") TaskStatus status);

}
