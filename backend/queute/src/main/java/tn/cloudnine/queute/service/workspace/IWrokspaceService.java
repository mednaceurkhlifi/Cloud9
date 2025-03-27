package tn.cloudnine.queute.service.workspace;

import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.WorkspaceRequest;
import tn.cloudnine.queute.model.workspace.Workspace;

public interface IWrokspaceService {
    Workspace createWorkspace(WorkspaceRequest request, MultipartFile image);
}
