package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.dto.workspace.UserDTO;
import tn.cloudnine.queute.dto.workspace.projections.ProjectUserProjection;
import tn.cloudnine.queute.dto.workspace.responses.ProjectUserResponse;
import tn.cloudnine.queute.model.workspace.ProjectUser;
import tn.cloudnine.queute.service.workspace.IProjectUserService;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("project-user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectUserController {

    private final IProjectUserService service;

    @PostMapping("add-project-manager")
    public ResponseEntity<UserDTO> addProjectManager(
            @RequestBody ProjectUser projectUser
    ) {
        return ResponseEntity.ok(service.addProjectManager(projectUser));
    }

    @PostMapping("add-project-member/{project_id}")
    public ResponseEntity<UserDTO> addProjectMember(
            @PathVariable("project_id") Long project_id,
            @RequestBody ProjectUser projectUser
    ) {
        return ResponseEntity.ok(service.addProjectMember(project_id, projectUser));
    }

    @DeleteMapping("delete-project-user/{project_id}/{user_email}")
    public ResponseEntity<Map<String, String>> deleteProjectUser(
            @PathVariable("project_id") Long project_id,
            @PathVariable("user_email") String user_email
    ) {
        service.deleteProjectUser(project_id, user_email);
        return ResponseEntity.ok().body(Map.of("status", "success", "message",  user_email + " removed from project with ID : " + project_id + "."));
    }


    @GetMapping("get-projects/{user_id}/{size}/{page_no}")
    public ResponseEntity<ProjectUserResponse> getProjects(
            @PathVariable("user_id") Long user_id,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
    ) {
        return ResponseEntity.ok(service.getProjects(user_id, size, page_no));
    }

    @GetMapping("get-projects/{user_email}")
    public ResponseEntity<Set<ProjectUserProjection>> getUserProjectsByEmail(
            @PathVariable("user_email") String user_email
    ) {
        return ResponseEntity.ok(service.getUserProjectsByEmail(user_email));
    }

    @GetMapping("get-project-team/{project_id}")
    public ResponseEntity<Set<ProjectUserProjection>> getProjectTeams(
            @PathVariable("project_id") Long project_id
    ) {
        return ResponseEntity.ok(service.getProjectTeams(project_id));
    }
}
