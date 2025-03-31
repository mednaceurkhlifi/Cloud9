package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.model.workspace.Task;
import tn.cloudnine.queute.service.workspace.ITaskService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService service;

    @PostMapping(value = "add-task/{project_id}", consumes = "multipart/form-data")
    public ResponseEntity<Task> addTaskToProject(
            @PathVariable("project_id") Long project_id,
            @RequestPart("task") Task task,
            @RequestPart(value = "documents", required = false) List<DocumentRequest> documents
            ) {
        return ResponseEntity.ok(service.addTaskToProject(project_id, task, documents));
    }

    @PatchMapping("update-task/{task_id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable("task_id") Long task_id,
            @RequestBody Task task
    ) {
        return ResponseEntity.ok(service.updateTask(task_id, task));
    }

    @DeleteMapping("delete-task/{task_id}")
    public ResponseEntity<Map<String, String>> deleteTask(
            @PathVariable("task_id") Long task_id
    ) {
        return ResponseEntity.ok(Map.of("status", "success", "message", "Task with ID " + task_id.toString() + " deleted successfully."));
    }

    @GetMapping("get-task-by-id/{task_id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable("task_id") Long task_id
    ) {
        return ResponseEntity.ok(service.getTaskById(task_id));
    }
}
