package tn.cloudnine.queute.service.workspace;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.UserDTO;
import tn.cloudnine.queute.dto.workspace.projections.TaskProjection;
import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.dto.workspace.responses.TaskResponse;
import tn.cloudnine.queute.model.workspace.Task;

import java.util.List;
import java.util.Set;

public interface ITaskService {
    Task addTaskToProject(Long projectId, Task task, List<DocumentRequest> documents_request, List<MultipartFile> documents);

    Task updateTask(Long taskId, Task request);

    Task getTaskById(Long taskId);

    Task addTaskToModule(Long moduleId, Task task, List<DocumentRequest> documents_request, List<MultipartFile> documents);

    TaskResponse getTasksByModule(Long moduleId, Integer size, Integer pageNo);

    TaskResponse getTasksByProject(Long projectId, Integer size, Integer pageNo);

    void deleteTask(Long taskId);

    UserDTO assignUserToTask(Long taskId, String userEmail);

    void removeUserFromTask(Long taskId, String userEmail);

    Set<Task> getTasksByUserEmail(String userEmail);
}
