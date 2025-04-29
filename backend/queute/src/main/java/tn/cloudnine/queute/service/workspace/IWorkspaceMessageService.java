package tn.cloudnine.queute.service.workspace;

import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.requests.DocumentRequest;
import tn.cloudnine.queute.dto.workspace.MessageDto;
import tn.cloudnine.queute.dto.workspace.responses.MessageResponse;
import tn.cloudnine.queute.model.workspace.WorkspaceMessage;

import java.util.List;

public interface IWorkspaceMessageService {
    MessageDto sendMessage(Long target, Long targetId, String sender_email, WorkspaceMessage message, List<DocumentRequest> attachmentRequest, List<MultipartFile> attachments);
    MessageResponse getWorkspaceMessages(Long workspaceId, Integer size, Integer page_no);
    MessageResponse getProjectMessages(Long projectId, Integer size, Integer page_no);
    MessageResponse getModuleMessages(Long moduleId, Integer size, Integer page_no);
    MessageResponse getTaskMessages(Long taskId, Integer size, Integer page_no);
}
