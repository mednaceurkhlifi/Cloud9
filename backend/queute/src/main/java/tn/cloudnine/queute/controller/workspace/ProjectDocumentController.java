package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import tn.cloudnine.queute.dto.requests.DocumentRequest;
import tn.cloudnine.queute.model.workspace.ProjectDocument;
import tn.cloudnine.queute.service.workspace.IProjectDocumentService;
import org.springframework.core.io.Resource;
import tn.cloudnine.queute.utils.IFileUploader;


import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("project-document")
@CrossOrigin(origins = "*")
public class ProjectDocumentController {

    private final IProjectDocumentService service;
    private final IFileUploader fileUploader;

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

    @PatchMapping(value = "add-documents-task/{task_id}", consumes = "multipart/form-data")
    public ResponseEntity<List<ProjectDocument>> addDocumentsToTask(
            @PathVariable("task_id") Long task_id,
            @RequestPart(value = "documents_request") List<DocumentRequest> documents_request,
            @RequestPart(value = "documents") List<MultipartFile> documents
    ) {
        return ResponseEntity.ok(service.addDocumentsToTask(
                task_id, documents_request, documents
        ));
    }

    @DeleteMapping("delete-document/{document_id}/{target_id}/{target}")
    public ResponseEntity<Map<String, String>> deleteDocument(
            @PathVariable("document_id") Long document_id,
            @PathVariable("target_id") Long targetId,
            @PathVariable("target") Integer target
    ) {
        return service.deleteDocument(document_id,targetId, target) ?
                ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Document with ID " + document_id + " deleted successfully."
                )) :
                ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Document with ID " + document_id + " not deleted."
                ));
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        return fileUploader.serveFile("images", filename, "image/jpeg");
    }

    @GetMapping("/documents/{filename}")
    public ResponseEntity<Resource> getDocument(@PathVariable("filename") String filename) {
        return fileUploader.serveFile("documents", filename, null);
    }

    @PostMapping(value = "add-documents-project-automation/{project_id}/{doc_name}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> addDocumentsToProjectAutomation(
            @PathVariable("project_id") Long project_id,
            @PathVariable("doc_name") String doc_name,
            @RequestPart(value = "document") MultipartFile document
    ) {
        service.addDocumentsToProjectAutomation(
                project_id, doc_name, document
        );
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "document saved."
        ));
    }
}
