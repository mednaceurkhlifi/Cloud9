package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.dto.workspace.UserDTO;
import tn.cloudnine.queute.dto.workspace.projections.ProjectProjection;
import tn.cloudnine.queute.dto.workspace.projections.ProjectUserProjection;
import tn.cloudnine.queute.dto.workspace.responses.ProjectUserResponse;
import tn.cloudnine.queute.enums.workspace.ProjectRole;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.repository.user.UserRepository;
import tn.cloudnine.queute.repository.workspace.ProjectRepository;
import tn.cloudnine.queute.repository.workspace.ProjectUserRepository;
import tn.cloudnine.queute.service.workspace.IProjectUserService;
import tn.cloudnine.queute.model.workspace.ProjectUser;


@Service
@RequiredArgsConstructor
public class ProjectUserService implements IProjectUserService {

    private final ProjectUserRepository repository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    public UserDTO addProjectManager(Long projectId, ProjectUser projectUser) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID : " + projectId)
        );
        if(repository.countByProjectProjectIdAndRole(projectId, ProjectRole.MANAGER) > 0) {
            throw new IllegalArgumentException(project.getName()  + " has already a manager");
        }
        User user = userRepository.findByEmailEquals(projectUser.getUser().getEmail()).orElseThrow(
                () -> new IllegalArgumentException("User not found with email : " + projectUser.getUser().getEmail())
        );
        projectUser.setUser(user);
        projectUser.setProject(project);
        repository.save(projectUser);
        return new UserDTO(
                user.getFull_name(), user.getEmail(), user.getImage(), ProjectRole.MANAGER
        );
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
}
