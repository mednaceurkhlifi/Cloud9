package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.dto.workspace.UserDTO;
import tn.cloudnine.queute.dto.workspace.projections.ProjectUserProjection;
import tn.cloudnine.queute.dto.workspace.responses.ProjectUserResponse;
import tn.cloudnine.queute.enums.workspace.ProjectRole;
import tn.cloudnine.queute.model.Embeddable.ProjectUserId;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.model.workspace.Task;
import tn.cloudnine.queute.repository.user.UserRepository;
import tn.cloudnine.queute.repository.workspace.ProjectRepository;
import tn.cloudnine.queute.repository.workspace.ProjectUserRepository;
import tn.cloudnine.queute.repository.workspace.TaskRepository;
import tn.cloudnine.queute.service.workspace.IProjectUserService;
import tn.cloudnine.queute.model.workspace.ProjectUser;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class ProjectUserService implements IProjectUserService {

    private final ProjectUserRepository repository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    public UserDTO addProjectManager(ProjectUser projectUser) {
        Project project = getProjectById(projectUser.getProject().getProjectId());

        if (repository.countByProjectProjectIdAndRole(projectUser.getProject().getProjectId(), ProjectRole.MANAGER) > 0) {
            throw new IllegalArgumentException(project.getName() + " already has a manager");
        }

        return saveProjectUser(project, projectUser, ProjectRole.MANAGER);
    }

    @Override
    public UserDTO addProjectMember(Long projectId, ProjectUser projectUser) {
        Project project = getProjectById(projectId);
        return saveProjectUser(project, projectUser, ProjectRole.TEAM_MEMBER);
    }

    @Override
    public void deleteProjectUser(Long projectId, String userEmail) {
        Project project = getProjectById(projectId);
        User user = getUserByEmail(userEmail);
        ProjectUser projectUser = repository.findById(new ProjectUserId(project.getProjectId(), user.getUserId())).orElseThrow(
                () -> new IllegalArgumentException("Project user not found")
        );
        Set<Task> tasks = taskRepository.findTasksByUserEmail(userEmail);
        for (Task t : tasks) {
            t.getMembers().remove(projectUser.getUser());
        }
        taskRepository.saveAll(tasks);
        repository.delete(projectUser);
    }

    @Override
    public Set<ProjectUserProjection> getProjectTeams(Long projectId) {
        return repository.findByProjectProjectId(projectId);
    }

    @Override
    public ProjectUserResponse getProjects(Long userId, Integer size, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, size);
        Page<ProjectUserProjection> page = repository.findByUserUserId(userId, pageable);

        return new ProjectUserResponse(
                page.toSet(), page.getNumber(),
                page.getSize(), page.getTotalElements(),
                page.getTotalPages(), page.isLast()
        );
    }

    /**
     * Util methods
     */
    private UserDTO saveProjectUser(Project project, ProjectUser projectUser, ProjectRole role) {
        String email = projectUser.getUser().getEmail();

        User user = getUserByEmail(email);
        if(repository.findById(new ProjectUserId(project.getProjectId(), user.getUserId())).isPresent()) {
            throw new IllegalArgumentException("User is already affected to this project");
        }
        projectUser.setId(new ProjectUserId(project.getProjectId(), user.getUserId()));
        projectUser.setUser(user);
        projectUser.setProject(project);
        projectUser.setRole(role);

        repository.save(projectUser);

        return new UserDTO(user.getFull_name(), user.getEmail(), user.getImage(), role);
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmailEquals(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

}
