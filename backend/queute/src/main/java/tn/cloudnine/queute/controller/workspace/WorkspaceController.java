package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.WorkspaceRequest;
import tn.cloudnine.queute.dto.workspace.responses.WorkspaceResponse;
import tn.cloudnine.queute.model.workspace.Workspace;
import tn.cloudnine.queute.service.workspace.IWrokspaceService;

@RestController
@RequestMapping("workspace")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class WorkspaceController {

    private final IWrokspaceService service;

    @PostMapping(value = "add-workspace", consumes = "multipart/form-data")
    public ResponseEntity<Workspace> createWorkspace(
            @RequestPart("workspace") Workspace workspace,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok().body(service.createWorkspace(workspace, image));
    }

    @PatchMapping(value = "update-workspace/{workspace_id}", consumes = "multipart/form-data")
    public ResponseEntity<Workspace> updateWorkspace(
            @PathVariable("workspace_id") Long workspace_id,
            @RequestPart("request") WorkspaceRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok().body(service.updateWorkspace(workspace_id, request, image));
    }

    @DeleteMapping(value = "delete-workspace/{workspace_id}")
    public void deleteWorkspace(@PathVariable("workspace_id") Long workspace_id) {
        service.deleteWorkspace(workspace_id);
    }

    @GetMapping(value = "get-workspace/{organization_id}/{size}/{page_no}")
    public ResponseEntity<WorkspaceResponse> getWorkspace(
            @PathVariable("organization_id") Long organization_id,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
    ) {
        return ResponseEntity.ok().body(service.getWorkspace(organization_id, size, page_no));
    }

}
