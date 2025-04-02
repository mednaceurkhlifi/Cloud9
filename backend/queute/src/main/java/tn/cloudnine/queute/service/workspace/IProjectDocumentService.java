package tn.cloudnine.queute.service.workspace;

import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.model.workspace.ProjectDocument;

import java.util.List;

public interface IProjectDocumentService {
    List<ProjectDocument> addDocumentsToProject(Long projectId, List<DocumentRequest> documentsRequest, List<MultipartFile> documents);

    List<ProjectDocument> addDocumentsToModule(Long moduleId, List<DocumentRequest> documentsRequest, List<MultipartFile> documents);

    List<ProjectDocument> addDocumentsToTask(Long taskId, List<DocumentRequest> documentsRequest, List<MultipartFile> documents);

    boolean deleteDocument(Long documentId);
}
