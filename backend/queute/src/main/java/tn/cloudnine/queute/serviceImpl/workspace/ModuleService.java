package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.model.workspace.ProjectModule;
import tn.cloudnine.queute.repository.workspace.ModuleRepository;
import tn.cloudnine.queute.repository.workspace.ProjectRepository;
import tn.cloudnine.queute.service.workspace.IModuleService;

@Service
@RequiredArgsConstructor
public class ModuleService implements IModuleService {

    private final ModuleRepository repository;
    private final ProjectRepository projectRepository;

    @Override
    public ProjectModule addModule(Long projectId, ProjectModule module) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID : " + projectId));
        project.getModules().add(module);
        projectRepository.save(project);
        return module;
    }

    @Override
    public ProjectModule updateModule(Long moduleId, ProjectModule request) {
        ProjectModule module = repository.findById(moduleId).orElseThrow(
                () -> new IllegalArgumentException("Module not found with ID : " + moduleId)
        );

        if(request.getTitle() != null && !request.getTitle().isEmpty()) {
            module.setTitle(request.getTitle());
        }
        if(request.getDescription() != null && !request.getDescription().isEmpty()) {
            module.setDescription(request.getDescription());
        }
        if(request.getPriority() != null) {
            module.setPriority(request.getPriority());
        }
        if(request.getBeginDate() != null) {
            module.setBeginDate(request.getBeginDate());
        }
        if(request.getDeadline() != null) {
            module.setDeadline(request.getDeadline());
        }

        return repository.save(module);
    }

    @Override
    public void deleteModule(Long moduleId) {
        ProjectModule module = repository.findById(moduleId).orElseThrow(
                () -> new IllegalArgumentException("Module not found with ID : " + moduleId)
        );
        repository.delete(module);
    }

}
