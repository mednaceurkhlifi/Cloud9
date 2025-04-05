package tn.cloudnine.queute.service.workspace;

import tn.cloudnine.queute.dto.workspace.UserDTO;
import tn.cloudnine.queute.dto.workspace.responses.ProjectUserResponse;
import tn.cloudnine.queute.model.workspace.ProjectUser;

public interface IProjectUserService {
    UserDTO addProjectManager(Long projectId, ProjectUser projectUser);

    ProjectUserResponse getProjects(Long userId, Integer size, Integer pageNo);
}
