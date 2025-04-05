package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.dto.workspace.UserDTO;
import tn.cloudnine.queute.dto.workspace.responses.ProjectUserResponse;
import tn.cloudnine.queute.model.workspace.ProjectUser;
import tn.cloudnine.queute.service.workspace.IProjectUserService;

@RestController
@RequestMapping("project-user")
@RequiredArgsConstructor
public class ProjectUserController {

    private final IProjectUserService service;

    @PostMapping("add-project-manager/{project_id}")
    public ResponseEntity<UserDTO> addProjectManager(
            @PathVariable("project_id") Long project_id,
            @RequestBody ProjectUser projectUser
    ) {
        return ResponseEntity.ok(service.addProjectManager(project_id, projectUser));
    }

    @GetMapping("get-projects/{user_id}/{size}/{page_no}")
    public ResponseEntity<ProjectUserResponse> getProjects(
            @PathVariable("user_id") Long user_id,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
    ) {
        return ResponseEntity.ok(service.getProjects(user_id, size, page_no));
    }
}
