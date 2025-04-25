package tn.cloudnine.queute.service.workspace;

import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.automation.ProjectRequest;
import tn.cloudnine.queute.dto.workspace.responses.ProjectResponse;
import tn.cloudnine.queute.model.workspace.Project;

public interface IProjectService {
    Project addProjectToWorkspace(Long workspaceId, Project project, MultipartFile image);

    Project updateProject(Project project, MultipartFile image, boolean imageOnUpdate);

    void deleteProject(Long projectId);

    Project getProjectById(Long projectId);
    ProjectResponse getProjectsByWorkspace(Long workspaceId, Integer size, Integer page_no);
    Float updateAchievement(Long projectId, long nbrCompleted, long nbrNotCompleted);

    Long automateProjectCreation(ProjectRequest request);
}
