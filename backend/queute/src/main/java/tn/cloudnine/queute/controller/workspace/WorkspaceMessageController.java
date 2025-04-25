package tn.cloudnine.queute.controller.workspace;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.MessageDto;
import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.dto.workspace.responses.MessageResponse;
import tn.cloudnine.queute.model.workspace.WorkspaceMessage;
import tn.cloudnine.queute.service.workspace.IWorkspaceMessageService;

import java.util.List;

@RestController
@RequestMapping("ws-msg")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class WorkspaceMessageController {

    private final IWorkspaceMessageService service;

    @PostMapping(value = "send-message/{target}/{targetId}/{sender_email}", consumes = "multipart/form-data")
    public ResponseEntity<MessageDto> sendMessage(
            @PathVariable("targetId") Long targetId,
            @PathVariable("target") Long target,
            @PathVariable("sender_email") String sender_email,
            @RequestPart("message")WorkspaceMessage message,
            @RequestPart(value = "attachment_request", required = false) List<DocumentRequest> attachment_request,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
            ) {
        return ResponseEntity.ok(service.sendMessage(target, targetId, sender_email, message, attachment_request, attachments));
    }

    @GetMapping("get-workspace-messages/{workspaceId}/{size}/{page_no}")
    public ResponseEntity<MessageResponse> getWorkspaceMessages(
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
    ) {
        return ResponseEntity.ok(service.getWorkspaceMessages(workspaceId,size, page_no));
    }

    @GetMapping("get-project-messages/{projectId}/{size}/{page_no}")
    public ResponseEntity<MessageResponse> getProjectMessages(
            @PathVariable("projectId") Long projectId,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
    ) {
        return ResponseEntity.ok(service.getProjectMessages(projectId,size, page_no));
    }

    @GetMapping("get-module-messages/{moduleId}/{size}/{page_no}")
    public ResponseEntity<MessageResponse> getModuleMessages(
            @PathVariable("moduleId") Long moduleId,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
    ) {
        return ResponseEntity.ok(service.getModuleMessages(moduleId,size, page_no));
    }

    @GetMapping("get-messages/{taskId}/{size}/{page_no}")
    public ResponseEntity<MessageResponse> getTaskMessages(
            @PathVariable("taskId") Long taskId,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
    ) {
        return ResponseEntity.ok(service.getTaskMessages(taskId,size, page_no));
    }
}
