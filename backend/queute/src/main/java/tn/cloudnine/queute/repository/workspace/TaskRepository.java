package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.workspace.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
