package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.dto.workspace.projections.ProjectModuleProjection;
import tn.cloudnine.queute.dto.workspace.responses.ModuleResponse;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.model.workspace.ProjectModule;
import tn.cloudnine.queute.repository.workspace.ModuleRepository;
import tn.cloudnine.queute.repository.workspace.ProjectRepository;
import tn.cloudnine.queute.repository.workspace.TaskRepository;
import tn.cloudnine.queute.service.workspace.IModuleService;
import tn.cloudnine.queute.service.workspace.ITaskService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ModuleService implements IModuleService {

    private final ModuleRepository repository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ITaskService taskService;

    @Override
    public ProjectModule addModule(Long projectId, ProjectModule module) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID : " + projectId));

        module.setProject(project);
        return repository.save(module);
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
        Set<Long> relatedTasks = taskRepository.findRelatedTaskIdByModule(moduleId);
        for (Long id : relatedTasks){
            taskService.deleteTask(id);
        }

        repository.delete(module);
    }

    @Override
    public ProjectModuleProjection getModuleById(Long moduleId) {
        return repository.findByModuleId(moduleId).orElseThrow(
                () -> new IllegalArgumentException("Module not found with ID : " + moduleId)
        );
    }

    @Override
    public ModuleResponse getModulesByProject(Long projectId, Integer size, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, size);
        Page<ProjectModule> modules = repository.findByProjectProjectId(projectId, pageable);

        return new ModuleResponse(
                modules.toList(), modules.getNumber(),
                modules.getSize(), modules.getTotalElements(),
                modules.getTotalPages(), modules.isLast()
        );
    }

}
