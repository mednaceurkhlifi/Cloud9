package tn.cloudnine.queute.service.workspace;

import tn.cloudnine.queute.dto.workspace.UserDTO;
import tn.cloudnine.queute.dto.workspace.projections.ProjectUserProjection;
import tn.cloudnine.queute.dto.workspace.responses.ProjectUserResponse;
import tn.cloudnine.queute.model.workspace.ProjectUser;

import java.util.Set;

public interface IProjectUserService {
    UserDTO addProjectManager(ProjectUser projectUser);

    ProjectUserResponse getProjects(Long userId, Integer size, Integer pageNo);

    UserDTO addProjectMember(Long projectId, ProjectUser projectUser);

    void deleteProjectUser(Long projectId, String userEmail);

    Set<ProjectUserProjection> getProjectTeams(Long projectId);

    Set<ProjectUserProjection> getUserProjectsByEmail(String userEmail);

    ProjectUserProjection getProjectUser(String userEmail, Long projectId);
}
