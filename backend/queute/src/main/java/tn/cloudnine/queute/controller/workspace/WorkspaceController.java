package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.WorkspaceRequest;
import tn.cloudnine.queute.model.workspace.Workspace;
import tn.cloudnine.queute.service.workspace.IWrokspaceService;

@RestController
@RequestMapping("workspace")
@RequiredArgsConstructor
public class WorkspaceController {

    private final IWrokspaceService service;

    @PostMapping(value = "add-workspace", consumes = "multipart/form-data")
    public ResponseEntity<Workspace> createWorkspace(
            @RequestPart("request") WorkspaceRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok().body(service.createWorkspace(request, image));
    }
}
