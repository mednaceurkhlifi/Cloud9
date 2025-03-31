package tn.cloudnine.queute.service.workspace;

import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.model.workspace.Task;

import java.util.List;

public interface ITaskService {
    Task addTaskToProject(Long projectId, Task task, List<DocumentRequest> documents);

    Task updateTask(Long taskId, Task request);

    Task getTaskById(Long taskId);
}
