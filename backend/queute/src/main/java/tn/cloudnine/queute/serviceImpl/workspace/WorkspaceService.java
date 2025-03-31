package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.WorkspaceRequest;
import tn.cloudnine.queute.dto.workspace.responses.ProjectResponse;
import tn.cloudnine.queute.dto.workspace.responses.WorkspaceResponse;
import tn.cloudnine.queute.dto.workspace.views.ProjectProjection;
import tn.cloudnine.queute.dto.workspace.views.WorkspaceProjection;
import tn.cloudnine.queute.model.organization.Organization;
import tn.cloudnine.queute.model.workspace.Workspace;
import tn.cloudnine.queute.repository.organization.OrganizationRepository;
import tn.cloudnine.queute.repository.workspace.ProjectRepository;
import tn.cloudnine.queute.repository.workspace.WorkspaceRepository;
import tn.cloudnine.queute.service.workspace.IWrokspaceService;
import tn.cloudnine.queute.utils.IFileUploader;

@Service
@RequiredArgsConstructor
public class WorkspaceService implements IWrokspaceService {

    private final WorkspaceRepository repository;
    private final OrganizationRepository organizationRepository;
    private final ProjectRepository projectRepository;
    private final IFileUploader fileUploader;
    private final String DEFAULT_IMAGE = "default_workspace.jpg";
    private final String DEFAULT_IMAGE_PROJECT = "default_project.jpg";

    @Override
    public Workspace createWorkspace(Workspace workspace, MultipartFile image) {
        Organization organization = organizationRepository.findById(workspace.getOrganization().getOrganization_id())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found with ID: " + workspace.getOrganization().getOrganization_id()));

        workspace.setOrganization(organization);

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
    public WorkspaceResponse getWorkspace(Long workspaceId, Integer size, Integer page_no) {
        WorkspaceProjection projection = repository.findByWorkspaceIdAndIsDeletedIsFalse(workspaceId).orElseThrow(
                () -> new IllegalArgumentException("Workspace not found with ID: " + workspaceId)
        );

        Pageable pageable = PageRequest.of(page_no, size);
        Page<ProjectProjection> projects = projectRepository.findByWorkspaceWorkspaceId(workspaceId, pageable);

        ProjectResponse projectResponse = new ProjectResponse(
                projects.toList(), projects.getNumber(),
                projects.getSize(), projects.getTotalElements(),
                projects.getTotalPages(), projects.isLast()
        );

        return new WorkspaceResponse(
                projection, projectResponse
        );
    }

}
