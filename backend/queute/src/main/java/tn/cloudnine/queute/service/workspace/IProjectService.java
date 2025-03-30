package tn.cloudnine.queute.service.workspace;

import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.model.workspace.Project;

public interface IProjectService {
    Project updateProject(Project project, MultipartFile image);
}
