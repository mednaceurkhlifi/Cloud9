package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.enums.DocumentType;
import tn.cloudnine.queute.model.workspace.Project;
import tn.cloudnine.queute.model.workspace.ProjectDocument;
import tn.cloudnine.queute.model.workspace.ProjectModule;
import tn.cloudnine.queute.model.workspace.Task;
import tn.cloudnine.queute.repository.workspace.ModuleRepository;
import tn.cloudnine.queute.repository.workspace.ProjectDocumentRepository;
import tn.cloudnine.queute.repository.workspace.ProjectRepository;
import tn.cloudnine.queute.repository.workspace.TaskRepository;
import tn.cloudnine.queute.service.workspace.IProjectDocumentService;
import tn.cloudnine.queute.utils.IFileUploader;

import java.util.ArrayList;
import java.util.List;

import static tn.cloudnine.queute.enums.DocumentType.OTHER;

@Service
@RequiredArgsConstructor
public class ProjectDocumentService implements IProjectDocumentService {

    private final ProjectDocumentRepository repository;
    private final ProjectRepository projectRepository;
    private final ModuleRepository moduleRepository;
    private final TaskRepository taskRepository;
    private final IFileUploader fileUploader;

    @Override
    public List<ProjectDocument> addDocumentsToProject(Long projectId, List<DocumentRequest> documentsRequest, List<MultipartFile> documents) {
        List<ProjectDocument> documentList = new ArrayList<>();
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID : " + projectId)
        );
        documentList = repository.saveAll(saveDocuments(documentsRequest, documents));
        project.getDocuments().addAll(documentList);
        projectRepository.save(project);
        return documentList;
    }

    @Override
    public List<ProjectDocument> addDocumentsToModule(Long moduleId, List<DocumentRequest> documentsRequest, List<MultipartFile> documents) {
        List<ProjectDocument> documentList = new ArrayList<>();
        ProjectModule module = moduleRepository.findById(moduleId).orElseThrow(
                () -> new IllegalArgumentException("Module not found with ID : " + moduleId)
        );
        documentList = repository.saveAll(saveDocuments(documentsRequest, documents));
        module.getDocuments().addAll(documentList);
        moduleRepository.save(module);
        return documentList;
    }

    @Override
    public List<ProjectDocument> addDocumentsToTask(Long taskId, List<DocumentRequest> documentsRequest, List<MultipartFile> documents) {
        List<ProjectDocument> documentList = new ArrayList<>();
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new IllegalArgumentException("Task not found with ID : " + taskId)
        );
        documentList = repository.saveAll(saveDocuments(documentsRequest, documents));
        task.getDocuments().addAll(documentList);
        taskRepository.save(task);
        return documentList;
    }

    @Override
    public boolean deleteDocument(Long documentId, Long targetId, Integer target) {
        ProjectDocument document = repository.findById(documentId).orElseThrow(
                () -> new IllegalArgumentException("Document not found with ID : " + documentId)
        );

        if (fileUploader.deleteFile(document.getPath(), document.getDocument_type())) {
            if (target == 1) {
                Project project = projectRepository.findById(targetId).orElseThrow(
                        () -> new IllegalArgumentException("Project not found with ID: " + targetId)
                );
                project.getDocuments().remove(document);
                projectRepository.save(project);
            } else if (target == 2) {
                ProjectModule module = moduleRepository.findById(targetId).orElseThrow(
                        () -> new IllegalArgumentException("Module not found with ID : " + documentId)
                );
                module.getDocuments().remove(document);
                moduleRepository.save(module);
            } else if (target == 3) {
                Task task = taskRepository.findById(targetId).orElseThrow(
                        () -> new IllegalArgumentException("Task not found with ID : " + documentId)
                );
                task.getDocuments().remove(document);
                taskRepository.save(task);
            }

            repository.delete(document);
            return true;
        }
        return false;
    }

    @Override
    public void addDocumentsToProjectAutomation(Long projectId, String docName, MultipartFile file) {
        ProjectDocument document = ProjectDocument.builder()
                .document_name(docName)
                .document_type(OTHER)
                .build();
        document.setPath(fileUploader.saveDocument(file));
        document = repository.save(document);
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID : " + projectId)
        );
        project.getDocuments().add(document);
        projectRepository.save(project);
    }


    /**
     * Util methods
     */
    private List<ProjectDocument> saveDocuments(
            List<DocumentRequest> documentsRequest, List<MultipartFile> documents
    ) {
        List<ProjectDocument> documentList = new ArrayList<>();
        if (!documentsRequest.isEmpty() && !documents.isEmpty()
                && documentsRequest.size() == documents.size()
        ) {
            for (int i = 0; i < documents.size(); i++) {
                ProjectDocument document = ProjectDocument.builder()
                        .document_name(documentsRequest.get(i).getDocument_name())
                        .document_type(documentsRequest.get(i).getDoc_type())
                        .build();

                switch (documentsRequest.get(i).getDoc_type()) {
                    case IMAGE -> {
                        document.setPath(fileUploader.saveImage(documents.get(i)));
                    }
                    case OTHER -> {
                        document.setPath(fileUploader.saveDocument(documents.get(i)));
                    }
                }
                documentList.add(document);
            }
        }
        return documentList;
    }
}
