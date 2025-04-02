package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.model.workspace.ProjectDocument;
import tn.cloudnine.queute.service.workspace.IProjectDocumentService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("project-document")
public class ProjectDocumentController {

    private final IProjectDocumentService service;

    @PatchMapping(value = "add-documents-project/{project_id}", consumes = "multipart/form-data")
    public ResponseEntity<List<ProjectDocument>> addDocumentsToProject(
            @PathVariable("project_id") Long project_id,
            @RequestPart(value = "documents_request") List<DocumentRequest> documents_request,
            @RequestPart(value = "documents") List<MultipartFile> documents
            ) {
        return ResponseEntity.ok(service.addDocumentsToProject(
                project_id, documents_request, documents
        ));
    }

    @PatchMapping(value = "add-documents-module/{module_id}", consumes = "multipart/form-data")
    public ResponseEntity<List<ProjectDocument>> addDocumentsToModule(
            @PathVariable("module_id") Long module_id,
            @RequestPart(value = "documents_request") List<DocumentRequest> documents_request,
            @RequestPart(value = "documents") List<MultipartFile> documents
    ) {
        return ResponseEntity.ok(service.addDocumentsToModule(
                module_id, documents_request, documents
        ));
    }

    @PatchMapping(value = "add-documents-module/{task_id}", consumes = "multipart/form-data")
    public ResponseEntity<List<ProjectDocument>> addDocumentsToTask(
            @PathVariable("task_id") Long task_id,
            @RequestPart(value = "documents_request") List<DocumentRequest> documents_request,
            @RequestPart(value = "documents") List<MultipartFile> documents
    ) {
        return ResponseEntity.ok(service.addDocumentsToTask(
                task_id, documents_request, documents
        ));
    }

    @DeleteMapping("delete-document/{document_id}")
    public ResponseEntity<Map<String, String>> deleteDocument(@PathVariable("document_id") Long document_id) {
        return service.deleteDocument(document_id) ?
                ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Document with ID " + document_id + " deleted successfully."
                )) :
                ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Document with ID " + document_id + " not deleted."
                ));
    }

}
