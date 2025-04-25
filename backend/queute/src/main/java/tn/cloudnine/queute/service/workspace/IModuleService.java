package tn.cloudnine.queute.service.workspace;

import tn.cloudnine.queute.dto.workspace.projections.ProjectModuleProjection;
import tn.cloudnine.queute.dto.workspace.responses.ModuleResponse;
import tn.cloudnine.queute.model.workspace.ProjectModule;

public interface IModuleService {
    ProjectModule addModule(Long projectId, ProjectModule module);

    ProjectModule updateModule(Long moduleId, ProjectModule request);
    void deleteModule(Long moduleId);

    ProjectModuleProjection getModuleById(Long moduleId);

    ModuleResponse getModulesByProject(Long projectId, Integer size, Integer pageNo);
}
