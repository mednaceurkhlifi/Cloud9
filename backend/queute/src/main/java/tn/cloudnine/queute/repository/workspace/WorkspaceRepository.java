package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.workspace.Workspace;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
