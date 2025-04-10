package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.dto.workspace.responses.ProjectResponse;
import tn.cloudnine.queute.model.workspace.Project;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.service.workspace.IProjectService;

import java.util.Map;

@RestController
@RequestMapping("project")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    private final IProjectService service;

    /**
     * Add Project to an existent workspace
     */
    @PostMapping(value = "add-project-workspace/{workspace_id}", consumes = "multipart/form-data")
    public ResponseEntity<Project> addProjectToWorkspace(
            @PathVariable("workspace_id") Long workspace_id,
            @RequestPart("project") Project project,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok().body(service.addProjectToWorkspace(workspace_id, project, image));
    }

    @PatchMapping(value = "update-project", consumes = "multipart/form-data")
    public ResponseEntity<Project> updateProject(
            @RequestPart("project") Project project,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("imageOnUpdate") boolean imageOnUpdate
    ) {
        return ResponseEntity.ok().body(service.updateProject(
                project, image, imageOnUpdate
        ));
    }

    @DeleteMapping("delete-project/{project_id}")
    public ResponseEntity<Map<String, String>> deleteProject(
           @PathVariable("project_id") Long project_id
    ) {
        service.deleteProject(project_id);
        return ResponseEntity.ok().body(Map.of("status", "success", "message", "Project with ID" + project_id.toString() + " deleted successfully."));
    }

    @GetMapping("get-project/{project_id}")
    public ResponseEntity<Project> getProjectById(
            @PathVariable("project_id") Long project_id
    ) {
        return ResponseEntity.ok(service.getProjectById(project_id));
    }

    @GetMapping(value = "get-projects/{workspace_id}/{size}/{page_no}")
    public ResponseEntity<ProjectResponse> getProjectsByWorkspace(
            @PathVariable("workspace_id") Long workspace_id,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
    ) {
        return ResponseEntity.ok().body(service.getProjectsByWorkspace(workspace_id, size, page_no));
    }

}
