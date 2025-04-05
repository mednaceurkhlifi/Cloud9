package tn.cloudnine.queute.service.workspace;

import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.WorkspaceRequest;
import tn.cloudnine.queute.dto.workspace.responses.WorkspaceResponse;
import tn.cloudnine.queute.model.workspace.Workspace;

public interface IWrokspaceService {
    Workspace createWorkspace(Workspace workspace, MultipartFile image);

    Workspace updateWorkspace(Long workspace_id, WorkspaceRequest request, MultipartFile image);

    void deleteWorkspace(Long workspaceId);

    WorkspaceResponse getWorkspace(Long organizationId, Integer size, Integer page_no);
}
