package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.dto.workspace.projections.ProjectModuleProjection;
import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.model.workspace.ProjectModule;
import tn.cloudnine.queute.model.workspace.Task;
import tn.cloudnine.queute.service.workspace.IModuleService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("project-module")
@RequiredArgsConstructor
public class ModuleController {

    private final IModuleService service;

    @PostMapping("add-module/{project_id}")
    public ResponseEntity<ProjectModule>  addModule(
            @PathVariable("project_id") Long project_id,
            @RequestBody ProjectModule module
            ) {
        return ResponseEntity.ok(service.addModule(project_id, module));
    }

    @PatchMapping("update-module/{module_id}")
    public ResponseEntity<ProjectModule>  updateModule(
            @PathVariable("module_id") Long module_id,
            @RequestBody ProjectModule request
    ) {
        return ResponseEntity.ok(service.updateModule(module_id, request));
    }

    @DeleteMapping("delete-module/{module_id}")
    public ResponseEntity<Map<String, String>> deleteModule(@PathVariable("module_id") Long module_id) {
        service.deleteModule(module_id);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Module with ID " + module_id + " deleted successfully."));
    }
}
