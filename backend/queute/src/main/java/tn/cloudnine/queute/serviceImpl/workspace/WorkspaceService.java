package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.WorkspaceRequest;
import tn.cloudnine.queute.model.organization.Organization;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.model.workspace.Workspace;
import tn.cloudnine.queute.repository.organization.OrganizationRepository;
import tn.cloudnine.queute.repository.workspace.WorkspaceRepository;
import tn.cloudnine.queute.service.workspace.IWrokspaceService;
import tn.cloudnine.queute.utils.IFileUploader;

@Service
@RequiredArgsConstructor
public class WorkspaceService implements IWrokspaceService {

    private final WorkspaceRepository repository;
    private final OrganizationRepository organizationRepository;
    private final IFileUploader fileUploader;
    private final String DEFAULT_IMAGE = "default_workspace.jpg";
    private final String DEFAULT_IMAGE_PROJECT = "default_project.jpg";

    @Override
    public Workspace createWorkspace(WorkspaceRequest request, MultipartFile image) {
        Organization organization = organizationRepository.findById(request.getOrganization())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found with ID: " + request.getOrganization()));

        Workspace workspace = Workspace.builder().name(request.getName())
                .description(request.getDescription())
                .organization(organization)
                .build();

        if (image != null && !image.isEmpty()) {
            workspace.setImage(fileUploader.saveImage(image));
        } else {
            workspace.setImage(DEFAULT_IMAGE);
        }

        return repository.save(workspace);

    }

    @Override
    public Workspace updateWorkspace(Long workspace_id, WorkspaceRequest request, MultipartFile image) {
        Workspace workspace = repository.findById(workspace_id).orElseThrow(
                () -> new IllegalArgumentException("Workspace not found with ID: " + workspace_id)
        );

        if(request.getName() != null && !request.getName().isEmpty()) {
            workspace.setName(request.getName());
        }
        if(request.getDescription() != null && !request.getDescription().isEmpty()) {
            workspace.setDescription(request.getDescription());
        }
        if (image != null && !image.isEmpty()) {
            if(!workspace.getImage().equals(DEFAULT_IMAGE))
                fileUploader.deleteFile(workspace.getImage());
            workspace.setImage(fileUploader.saveImage(image));
        }

        return workspace;
    }

    @Override
    public void deleteWorkspace(Long workspaceId) {
        repository.deleteById(workspaceId);
    }

    @Override
    public Workspace getWorkspace(Long workspaceId) {
        return repository.findById(workspaceId).orElseThrow(
                () -> new IllegalArgumentException("Workspace not found with ID: " + workspaceId)
        );
    }
    /**
     * Add Project to an existent workspace
     */
    @Override
    public Workspace addProjectToWorkspace(Long workspaceId, Project project, MultipartFile image) {
        Workspace workspace = repository.findById(workspaceId).orElseThrow(
                () -> new IllegalArgumentException("Workspace not found with ID: " + workspaceId)
        );

        if (image != null && !image.isEmpty()) {
            project.setImage(fileUploader.saveImage(image));
        } else {
            project.setImage(DEFAULT_IMAGE_PROJECT);
        }

        workspace.getProjects().add(project);

        return repository.save(workspace);
    }
}
