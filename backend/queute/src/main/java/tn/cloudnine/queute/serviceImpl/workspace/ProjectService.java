package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.automation.ModuleRequest;
import tn.cloudnine.queute.dto.workspace.automation.ProjectRequest;
import tn.cloudnine.queute.dto.workspace.automation.TaskRequest;
import tn.cloudnine.queute.dto.workspace.responses.ProjectResponse;
import tn.cloudnine.queute.dto.workspace.projections.ProjectProjection;
import tn.cloudnine.queute.enums.DocumentType;
import tn.cloudnine.queute.enums.workspace.ProjectStatus;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.model.workspace.ProjectModule;
import tn.cloudnine.queute.model.workspace.Task;
import tn.cloudnine.queute.model.workspace.Workspace;
import tn.cloudnine.queute.repository.workspace.*;
import tn.cloudnine.queute.service.workspace.IModuleService;
import tn.cloudnine.queute.service.workspace.IProjectService;
import tn.cloudnine.queute.service.workspace.ITaskService;
import tn.cloudnine.queute.utils.IFileUploader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static tn.cloudnine.queute.enums.workspace.ProjectStatus.FINISHED;

@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

    private final ProjectRepository repository;
    private final ProjectUserRepository projectUserRepository;
    private final TaskRepository taskRepository;
    private final ModuleRepository moduleRepository;
    private final WorkspaceRepository workspaceRepository;
    private final IFileUploader fileUploader;
    private final IModuleService moduleService;
    private final ITaskService taskService;
    private final String DEFAULT_IMAGE = "default_project.jpg";

    @Override
    public Project updateProject(Project incomingProject, MultipartFile image, boolean imageOnUpdate) {
        Project existingProject = repository.findById(incomingProject.getProjectId()).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID: " + incomingProject.getProjectId())
        );

        if (incomingProject.getName() != null && !incomingProject.getName().isEmpty()) {
            existingProject.setName(incomingProject.getName());
        }
        if (incomingProject.getDescription() != null && !incomingProject.getDescription().isEmpty()) {
            existingProject.setDescription(incomingProject.getDescription());
        }
        if (incomingProject.getPriority() != null) {
            existingProject.setPriority(incomingProject.getPriority());
        }
        if (incomingProject.getBeginDate() != null) {
            existingProject.setBeginDate(incomingProject.getBeginDate());
        }
        if (incomingProject.getDeadline() != null) {
            existingProject.setDeadline(incomingProject.getDeadline());
        }
        if (incomingProject.getStatus() != null) {
            if(incomingProject.getStatus().equals(FINISHED) && existingProject.getAchievement() < 100f)
                throw new IllegalArgumentException("You need to achieve all project task first before setting its status to finished.");
            existingProject.setStatus(incomingProject.getStatus());
        }
        if(imageOnUpdate) {
            if (image != null && !image.isEmpty()) {
                if (!existingProject.getImage().equals(DEFAULT_IMAGE))
                    fileUploader.deleteFile(existingProject.getImage(), DocumentType.IMAGE);
                existingProject.setImage(fileUploader.saveImage(image));
            } else {
                if (!existingProject.getImage().equals(DEFAULT_IMAGE))
                    fileUploader.deleteFile(existingProject.getImage(), DocumentType.IMAGE);
                existingProject.setImage(DEFAULT_IMAGE);
            }
        }

        return repository.save(existingProject);
    }

    @Override
    public void deleteProject(Long projectId) {
        Project project = repository.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID: " + projectId)
        );
        Set<Long> relatedModules = moduleRepository.findRelatedModuleIdByProject(projectId);
        Set<Long> relatedTasks = taskRepository.findRelatedTaskIdByProject(projectId);
        for (Long id : relatedModules){
            moduleService.deleteModule(id);
        }
        for (Long id : relatedTasks){
            taskService.deleteTask(id);
        }
        projectUserRepository.deleteRelatedProjectProjectUser(projectId);
        if(!project.getImage().equals(DEFAULT_IMAGE))
            fileUploader.deleteFile(project.getImage(), DocumentType.IMAGE);

        repository.delete(project);
    }

    @Override
    public Project getProjectById(Long projectId) {
        return repository.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID: " + projectId)
        );
    }

    /**
     * Add Project to an existent workspace
     */
    @Override
    public Project addProjectToWorkspace(Long workspaceId, Project project, MultipartFile image) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(
                () -> new IllegalArgumentException("Workspace not found with ID: " + workspaceId)
        );
        if (image != null && !image.isEmpty()) {
            project.setImage(fileUploader.saveImage(image));
        } else {
            project.setImage(DEFAULT_IMAGE);
        }
        project.setAchievement(0f);
        project.setWorkspace(workspace);
        return repository.save(project);
    }
    /**
     * Get projects related to an existent workspace
     */
    @Override
    public ProjectResponse getProjectsByWorkspace(Long workspaceId, Integer size, Integer page_no) {
        Pageable pageable = PageRequest.of(page_no, size);
        Page<ProjectProjection> projects = repository.findByWorkspaceWorkspaceId(workspaceId, pageable);

        return  new ProjectResponse(
                projects.toList(), projects.getNumber(),
                projects.getSize(), projects.getTotalElements(),
                projects.getTotalPages(), projects.isLast()
        );
    }

    /**
     * Update achievement
     */
    @Override
    public Float updateAchievement(Long projectId, long nbrCompleted, long nbrNotCompleted) {
        Project project = repository.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID: " + projectId)
        );
        long tot = nbrCompleted + nbrNotCompleted;
        float achievement = ((float) tot / 100) * nbrCompleted;
        float roundedAchievement = BigDecimal.valueOf(achievement)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();
        project.setAchievement(roundedAchievement);
        repository.save(project);
        return achievement;
    }

    @Override
    public Long automateProjectCreation(ProjectRequest request) {
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .priority(request.getPriority())
                .achievement(request.getAchievement())
                .beginDate(request.getBeginDate())
                .image(DEFAULT_IMAGE)
                .deadline(request.getDeadline())
                .status(request.getStatus())
                .build();
        Workspace workspace = workspaceRepository.findById(request.getWorkspace_id()).orElseThrow(
                () -> new IllegalArgumentException("Workspace not found with ID: " + request.getWorkspace_id())
        );
        project.setWorkspace(workspace);
        project = repository.save(project);
        this.fillModulesAndTasks(request.getModules(), project);
        return project.getProjectId();
    }

    /**
     * Util methods
     */
    private void fillModulesAndTasks(List<ModuleRequest> requests, Project project) {
        for (ModuleRequest request : requests) {
            ProjectModule module = ProjectModule.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .achievement(request.getAchievement())
                    .priority(request.getPriority())
                    .beginDate(request.getBeginDate())
                    .project(project)
                    .deadline(request.getDeadline()).build();
            module = moduleRepository.save(module);
            for (TaskRequest taskRequest : request.getTasks()){
                Task task = Task.builder().title(taskRequest.getTitle())
                        .description(taskRequest.getDescription())
                        .priority(taskRequest.getPriority())
                        .status(taskRequest.getStatus())
                        .module(module)
                        .beginDate(taskRequest.getBeginDate())
                        .deadline(taskRequest.getDeadline()).build();
                taskRepository.save(task);
            }
        }
    }
}
