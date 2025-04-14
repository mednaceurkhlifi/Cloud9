package tn.cloudnine.queute.service.workspace;

import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.workspace.requests.DocumentRequest;
import tn.cloudnine.queute.dto.workspace.responses.MessageResponse;
import tn.cloudnine.queute.model.workspace.WorkspaceMessage;

import java.util.List;

public interface IWorkspaceMessageService {
    WorkspaceMessage sendMessage(Long target, Long targetId,WorkspaceMessage message, List<DocumentRequest> attachmentRequest, List<MultipartFile> attachments);
    MessageResponse getWorkspaceMessages(Long workspaceId, Integer size, Integer page_no);
    MessageResponse getProjectMessages(Long projectId, Integer size, Integer page_no);
    MessageResponse getModuleMessages(Long moduleId, Integer size, Integer page_no);
    MessageResponse getTaskMessages(Long taskId, Integer size, Integer page_no);
}
