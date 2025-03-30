package tn.cloudnine.queute.service.workspace;

import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.WorkspaceRequest;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.model.workspace.Workspace;

public interface IWrokspaceService {
    Workspace createWorkspace(WorkspaceRequest request, MultipartFile image);

    Workspace updateWorkspace(Long workspace_id, WorkspaceRequest request, MultipartFile image);

    void deleteWorkspace(Long workspaceId);

    Workspace getWorkspace(Long workspaceId);

    Workspace addProjectToWorkspace(Long workspaceId, Project project, MultipartFile image);
}
