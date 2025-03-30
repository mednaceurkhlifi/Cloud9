package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.model.workspace.Project;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.service.workspace.IProjectService;

@RestController
@RequestMapping("project")
@RequiredArgsConstructor
public class ProjectController {

    private final IProjectService service;

    @PatchMapping(value = "update-project", consumes = "multipart/form-data")
    public ResponseEntity<Project> updateProject(
            @RequestPart("project") Project project,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok().body(service.updateProject(
                project, image
        ));
    }
}
