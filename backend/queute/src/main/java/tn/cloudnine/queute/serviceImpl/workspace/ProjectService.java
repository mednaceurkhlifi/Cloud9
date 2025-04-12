package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.responses.ProjectResponse;
import tn.cloudnine.queute.dto.workspace.projections.ProjectProjection;
import tn.cloudnine.queute.enums.DocumentType;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.model.workspace.Workspace;
import tn.cloudnine.queute.repository.workspace.ProjectRepository;
import tn.cloudnine.queute.repository.workspace.WorkspaceRepository;
import tn.cloudnine.queute.service.workspace.IProjectService;
import tn.cloudnine.queute.utils.IFileUploader;

@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

    private final ProjectRepository repository;
    private final WorkspaceRepository workspaceRepository;
    private final IFileUploader fileUploader;
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
}
