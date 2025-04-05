package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.UserDTO;
import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.dto.workspace.responses.TaskResponse;
import tn.cloudnine.queute.model.workspace.Task;
import tn.cloudnine.queute.service.workspace.ITaskService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService service;

    @PostMapping(value = "add-task-project/{project_id}", consumes = "multipart/form-data")
    public ResponseEntity<Task> addTaskToProject(
            @PathVariable("project_id") Long project_id,
            @RequestPart("task") Task task,
            @RequestPart(value = "documents_request", required = false) List<DocumentRequest> documents_request,
            @RequestPart(value = "documents", required = false) List<MultipartFile> documents
            ) {
        return ResponseEntity.ok(service.addTaskToProject(project_id, task, documents_request, documents));
    }

    @PostMapping(value = "add-task-module/{module_id}", consumes = "multipart/form-data")
    public ResponseEntity<Task> addTaskToModule(
            @PathVariable("module_id") Long module_id,
            @RequestPart("task") Task task,
            @RequestPart(value = "documents_request", required = false) List<DocumentRequest> documents_request,
            @RequestPart(value = "documents", required = false) List<MultipartFile> documents
    ) {
        return ResponseEntity.ok(service.addTaskToModule(module_id, task, documents_request, documents));
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
        service.deleteTask(task_id);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Task with ID " + task_id + " deleted successfully."));
    }

    @GetMapping("get-task-by-id/{task_id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable("task_id") Long task_id
    ) {
        return ResponseEntity.ok(service.getTaskById(task_id));
    }

    @GetMapping("get-task-by-module/{module_id}/{size}/{page_no}")
    public ResponseEntity<TaskResponse> getTasksByModule(
            @PathVariable("module_id") Long module_id,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
            ) {
        return ResponseEntity.ok(service.getTasksByModule(module_id, size, page_no));
    }

    @GetMapping("get-task-by-project/{project_id}/{size}/{page_no}")
    public ResponseEntity<TaskResponse> getTaskByProject(
            @PathVariable("project_id") Long project_id,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
    ) {
        return ResponseEntity.ok(service.getTasksByProject(project_id, size, page_no));
    }

    @PatchMapping("assign-user/{task_id}/{user_email}")
    public ResponseEntity<UserDTO> assignUserToTask(
            @PathVariable("task_id") Long task_id,
            @PathVariable("user_email") String user_email
    ) {
        return ResponseEntity.ok(service.assignUserToTask(task_id, user_email));
    }

    @PatchMapping("remove-user/{task_id}/{user_email}")
    public ResponseEntity<Map<String, String>> removeUserFromTask(
            @PathVariable("task_id") Long task_id,
            @PathVariable("user_email") String user_email
    ) {
        service.removeUserFromTask(task_id, user_email);
        return ResponseEntity.ok(
                Map.of("status", "success", "message", "User removed successfully.")
        );
    }
}
