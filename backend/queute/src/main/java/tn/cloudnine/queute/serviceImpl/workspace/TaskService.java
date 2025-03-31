package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.model.workspace.ProjectDocument;
import tn.cloudnine.queute.model.workspace.Task;
import tn.cloudnine.queute.repository.workspace.ProjectRepository;
import tn.cloudnine.queute.repository.workspace.TaskRepository;
import tn.cloudnine.queute.service.workspace.ITaskService;
import tn.cloudnine.queute.utils.IFileUploader;

import java.util.List;

import static tn.cloudnine.queute.enums.DocumentType.IMAGE;
import static tn.cloudnine.queute.enums.DocumentType.OTHER;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository repository;
    private final ProjectRepository projectRepository;
    private final IFileUploader fileUploader;

    @Override
    public Task addTaskToProject(Long projectId, Task task, List<DocumentRequest> documents) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID : " + projectId)
        );
        saveDocuments(task, documents);
        task = repository.save(task);
        project.getTasks().add(repository.save(task));
        projectRepository.save(project);
        return task;
    }

    @Override
    public Task updateTask(Long taskId, Task request) {
        Task task = repository.findById(taskId).orElseThrow(
                () -> new IllegalArgumentException("Task not found with ID : " + taskId)
        );

        if(request.getTitle() != null && !request.getTitle().isEmpty()) {
            task.setTitle(request.getTitle());
        }
        if(request.getDescription() != null && !request.getDescription().isEmpty()) {
            task.setDescription(request.getDescription());
        }
        if(request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if(request.getBeginDate() != null) {
            task.setBeginDate(request.getBeginDate());
        }
        if(request.getDeadline() != null) {
            task.setDeadline(request.getDeadline());
        }
        if(request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        return repository.save(task);

    }

    @Override
    public Task getTaskById(Long taskId) {
        return repository.findById(taskId).orElseThrow(
                () -> new IllegalArgumentException("Task not found with ID : " + taskId)
        );
    }

    /**
     * Util methods
     */
    private void saveDocuments(Task task, List<DocumentRequest> documents) {
        if(documents != null && !documents.isEmpty()) {
            for (DocumentRequest doc : documents) {

                ProjectDocument document = ProjectDocument.builder()
                        .document_name(doc.getDocument_name())
                        .document_type(doc.getDoc_type())
                        .build();

                switch (doc.getDoc_type()) {
                    case IMAGE -> {
                        document.setPath(fileUploader.saveImage(doc.getDocument()));
                        task.getDocuments().add(document);
                    }
                    case OTHER -> {
                        document.setPath(fileUploader.saveDocument(doc.getDocument()));
                        task.getDocuments().add(document);
                    }
                }
            }
        }
    }

}
