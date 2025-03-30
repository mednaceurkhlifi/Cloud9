package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.repository.workspace.ProjectRepository;
import tn.cloudnine.queute.service.workspace.IProjectService;
import tn.cloudnine.queute.utils.IFileUploader;

@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

    private final ProjectRepository repository;
    private final IFileUploader fileUploader;
    private final String DEFAULT_IMAGE = "default_project.jpg";

    @Override
    public Project updateProject(Project incomingProject, MultipartFile image) {
        Project existingProject = repository.findById(incomingProject.getProject_id()).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID: " + incomingProject.getProject_id())
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
        if (incomingProject.getBegin_date() != null) {
            existingProject.setBegin_date(incomingProject.getBegin_date());
        }
        if (incomingProject.getDeadline() != null) {
            existingProject.setDeadline(incomingProject.getDeadline());
        }
        if (incomingProject.getStatus() != null) {
            existingProject.setStatus(incomingProject.getStatus());
        }
        if (image != null && !image.isEmpty()) {
            if (!existingProject.getImage().equals(DEFAULT_IMAGE))
                fileUploader.deleteFile(existingProject.getImage());
            existingProject.setImage(fileUploader.saveImage(image));
        }

        return repository.save(existingProject);
    }
}
