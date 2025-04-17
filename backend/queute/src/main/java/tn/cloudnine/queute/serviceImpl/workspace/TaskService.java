package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.UserDTO;
import tn.cloudnine.queute.dto.workspace.projections.TaskProjection;
import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.dto.workspace.responses.TaskResponse;
import tn.cloudnine.queute.enums.workspace.ProjectStatus;
import tn.cloudnine.queute.enums.workspace.TaskStatus;
import tn.cloudnine.queute.model.Embeddable.ProjectUserId;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.model.workspace.*;
import tn.cloudnine.queute.repository.user.UserRepository;
import tn.cloudnine.queute.repository.workspace.ModuleRepository;
import tn.cloudnine.queute.repository.workspace.ProjectRepository;
import tn.cloudnine.queute.repository.workspace.ProjectUserRepository;
import tn.cloudnine.queute.repository.workspace.TaskRepository;
import tn.cloudnine.queute.service.workspace.ITaskService;
import tn.cloudnine.queute.utils.IFileUploader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static tn.cloudnine.queute.enums.workspace.ProjectStatus.*;
import static tn.cloudnine.queute.enums.workspace.ProjectStatus.IN_PROGRESS;
import static tn.cloudnine.queute.enums.workspace.TaskStatus.*;


@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository repository;
    private final ProjectRepository projectRepository;
    private final ModuleRepository moduleRepository;
    private final ProjectUserRepository projectUserRepository;
    private final UserRepository userRepository;
    private final IFileUploader fileUploader;

    @Override
    public Task addTaskToProject(Long projectId, Task task, List<DocumentRequest> documents_request, List<MultipartFile> documents) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID : " + projectId)
        );
        saveDocuments(task, documents_request, documents);
        task.setProject(project);
        task = repository.save(task);
        updateAchievement(task.getProject().getProjectId(), 0L);
        return task;
    }

    @Override
    public Task addTaskToModule(Long moduleId, Task task, List<DocumentRequest> documents_request, List<MultipartFile> documents) {
        ProjectModule module = moduleRepository.findById(moduleId).orElseThrow(
                () -> new IllegalArgumentException("Module not found with ID : " + moduleId)
        );
        saveDocuments(task, documents_request, documents);
        task.setModule(module);
        task = repository.save(task);
        updateAchievement(0L, task.getModule().getModuleId());
        return task;
    }

    @Override
    public Task updateTask(Long taskId, Task request) {
        Task task = repository.findById(taskId).orElseThrow(
                () -> new IllegalArgumentException("Task not found with ID : " + taskId)
        );
        if (hasText(request.getTitle())) {
            task.setTitle(request.getTitle());
        }
        if (hasText(request.getDescription())) {
            task.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getBeginDate() != null) {
            task.setBeginDate(request.getBeginDate());
        }
        if (request.getDeadline() != null) {
            task.setDeadline(request.getDeadline());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        task = repository.save(task);
        if (request.getStatus() != null) {
            if (task.getProject() != null) {
                updateAchievement(task.getProject().getProjectId(), 0L);
            } else if (task.getModule() != null) {
                updateAchievement(0L, task.getModule().getModuleId());
            }
        }
        return task;
    }

    @Override
    public Task getTaskById(Long taskId) {
        return repository.findById(taskId).orElseThrow(
                () -> new IllegalArgumentException("Task not found with ID : " + taskId)
        );
    }

    @Override
    public TaskResponse getTasksByModule(Long moduleId, Integer size, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, size);
        Page<TaskProjection> tasks = repository.findAllByModuleModuleId(moduleId, pageable);

        return new TaskResponse(
                tasks.toList(), tasks.getNumber(),
                tasks.getSize(), tasks.getTotalElements(),
                tasks.getTotalPages(), tasks.isLast()
        );
    }

    @Override
    public TaskResponse getTasksByProject(Long projectId, Integer size, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, size);
        Page<TaskProjection> tasks = repository.findAllByProjectProjectId(projectId, pageable);

        return new TaskResponse(
                tasks.toList(), tasks.getNumber(),
                tasks.getSize(), tasks.getTotalElements(),
                tasks.getTotalPages(), tasks.isLast()
        );
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = repository.findById(taskId).orElseThrow(
                () -> new IllegalArgumentException("Task not found with ID : " + taskId)
        );
        repository.delete(task);
        if(task.getProject() != null)
            updateAchievement(task.getProject().getProjectId(), 0L);
        else
            updateAchievement(0L, task.getModule().getModuleId());
    }

    @Override
    public UserDTO assignUserToTask(Long taskId, String userEmail) {
        Task task = repository.findById(taskId).orElseThrow(
                () -> new IllegalArgumentException("Task not found with ID : " + taskId)
        );
        User user = userRepository.findByEmailEquals(userEmail).orElseThrow(
                () -> new IllegalArgumentException("User not found with email : " + userEmail)
        );
        if (task.getMembers().contains(user))
            throw new IllegalArgumentException("User already assigned to task.");
        Project project;
        if (task.getProject() != null) {
            project = task.getProject();
        } else {
            project = task.getModule().getProject();
        }
        ProjectUser projectUser = projectUserRepository.findById(
                new ProjectUserId(project.getProjectId(), user.getUserId())
        ).orElseThrow(
                () -> new IllegalArgumentException("You need to add " + userEmail + " to the project first.")
        );
        task.getMembers().add(user);
        repository.save(task);
        return new UserDTO(user.getFull_name(), user.getEmail(), user.getImage(), projectUser.getRole());
    }

    @Override
    public void removeUserFromTask(Long taskId, String userEmail) {
        Task task = repository.findTasksByIdAndUserEmail(taskId, userEmail).orElseThrow(
                () -> new IllegalArgumentException("User is not assigned to task or the task not found with ID : " + taskId)
        );
        task.getMembers().stream()
                .filter(u -> u.getEmail().equals(userEmail))
                .findFirst().ifPresent(user -> task.getMembers().remove(user));
        repository.save(task);
    }

    @Override
    public Set<Task> getTasksByUserEmail(String userEmail) {
        User user = userRepository.findByEmailEquals(userEmail).orElseThrow(
                () -> new IllegalArgumentException("User not found with email : " + userEmail)
        );
        return repository.findTasksByUserEmail(user.getEmail());
    }

    /**
     * Util methods
     */
    private boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private void saveDocuments(
            Task task, List<DocumentRequest> documentsRequest, List<MultipartFile> documents
    ) {
        if (documentsRequest != null && documents != null &&
                !documentsRequest.isEmpty() && !documents.isEmpty()
                && documentsRequest.size() == documents.size()
        ) {
            for (int i = 0; i < documents.size(); i++) {
                ProjectDocument document = ProjectDocument.builder()
                        .document_name(documentsRequest.get(i).getDocument_name())
                        .document_type(documentsRequest.get(i).getDoc_type())
                        .build();

                switch (documentsRequest.get(i).getDoc_type()) {
                    case IMAGE -> {
                        document.setPath(fileUploader.saveImage(documents.get(i)));
                    }
                    case OTHER -> {
                        document.setPath(fileUploader.saveDocument(documents.get(i)));
                    }
                }
                task.getDocuments().add(document);
            }
        }
    }

    /**
     * Update achievement
     */
    private Float updateAchievement(Long projectId, Long moduleId) {
        if (projectId > 0) {
            return updateProjectAchievement(projectId);
        } else {
            return updateModuleAndProjectAchievement(moduleId);
        }
    }

    private Float updateProjectAchievement(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID : " + projectId)
        );

        Set<Long> moduleIds = getModuleIdsByProject(projectId);

        long completed = repository.countByProjectProjectIdAndStatus(projectId, DONE)
                + repository.countByModulesAndStatus(moduleIds, DONE);
        long notCompleted = repository.countByProjectProjectIdAndStatusNot(projectId, DONE)
                + repository.countByModulesAndStatusNotEquals(moduleIds, DONE);

        float achievement = calculateAchievement(completed, notCompleted);
        project.setAchievement(achievement);
        if(achievement == 100f) {
            project.setStatus(FINISHED);
        } else {
            project.setStatus(IN_PROGRESS);
        }
        projectRepository.save(project);

        return achievement;
    }

    private Float updateModuleAndProjectAchievement(Long moduleId) {
        ProjectModule module = moduleRepository.findById(moduleId).orElseThrow(
                () -> new IllegalArgumentException("Module not found with ID : " + moduleId)
        );

        // Update module achievement
        long completed = repository.countByModuleModuleIdAndStatus(moduleId, DONE);
        long notCompleted = repository.countByModuleModuleIdAndStatusNot(moduleId, DONE);
        float moduleAchievement = calculateAchievement(completed, notCompleted);

        module.setAchievement(moduleAchievement);
        moduleRepository.save(module);

        // Update related project achievement
        Long projectId = module.getProject().getProjectId();
        updateProjectAchievement(projectId);

        return moduleAchievement;
    }

    private Set<Long> getModuleIdsByProject(Long projectId) {
        return moduleRepository.findByProjectProjectId(projectId).stream()
                .map(ProjectModule::getModuleId)
                .collect(Collectors.toSet());
    }

    private float calculateAchievement(long completed, long notCompleted) {
        long total = completed + notCompleted;
        if (total == 0) return 0f;

        float achievement = ((float) completed / total) * 100;
        return BigDecimal.valueOf(achievement)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();
    }



}
