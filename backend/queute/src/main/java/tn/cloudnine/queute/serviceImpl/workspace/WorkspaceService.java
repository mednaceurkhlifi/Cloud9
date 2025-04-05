package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.WorkspaceRequest;
import tn.cloudnine.queute.model.organization.Organization;
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
}
