package tn.cloudnine.queute.serviceImpl.workspace;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.requests.DocumentRequest;
import tn.cloudnine.queute.dto.workspace.MessageDto;
import tn.cloudnine.queute.dto.workspace.UserDTO;
import tn.cloudnine.queute.dto.workspace.projections.MessageProjection;
import tn.cloudnine.queute.dto.workspace.responses.MessageResponse;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.model.workspace.*;
import tn.cloudnine.queute.repository.user.UserRepository;
import tn.cloudnine.queute.repository.workspace.*;
import tn.cloudnine.queute.service.workspace.IWorkspaceMessageService;
import tn.cloudnine.queute.utils.IFileUploader;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceMessageService implements IWorkspaceMessageService {

    private final WorkspaceMessageRepository repository;
    private final ProjectDocumentRepository documentRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ProjectRepository projectRepository;
    private final ModuleRepository moduleRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final IFileUploader fileUploader;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public MessageDto sendMessage(Long target, Long targetId, String sender_email, WorkspaceMessage message, List<DocumentRequest> attachmentRequest, List<MultipartFile> attachments) {
        User sender = userRepository.findByEmailEquals(sender_email).orElseThrow(
                () -> new IllegalArgumentException("User not found with email : " + sender_email)
        );
        message.setSender(sender);
        List<ProjectDocument> attachmentList = new ArrayList<>();
        attachmentList = documentRepository.saveAll(saveDocuments(attachmentRequest, attachments));
        message.setAttachments(attachmentList);
        message = repository.save(message);
        switch (target.intValue()) {
            case 1 -> {
                this.attachMessageToWorkspace(message, targetId);
            }
            case 2 -> {
                this.attachMessageToProject(message, targetId);
            }
            case 3 -> {
                this.attachMessageToModule(message, targetId);
            }
            case 4 -> {
                this.attachMessageToTask(message, targetId);
            }
        }
        String destination = getTargetName(target);
        MessageDto messageDto = new MessageDto(
                getUserDto(message.getSender()), message.getMessage(), message.getAttachments(), message.getCreatedAt()
        );
        messagingTemplate.convertAndSendToUser(targetId.toString(), destination, messageDto);
        return messageDto;
    }

    @Override
    public MessageResponse getWorkspaceMessages(Long workspaceId, Integer size, Integer page_no) {
        Pageable pageable = PageRequest.of(page_no, size);
        Page<MessageProjection> page = repository.findByWorkspaceWorkspaceId(workspaceId, pageable);
        return new MessageResponse(
                page.toList(), page.getNumber(),
                page.getSize(), page.getTotalElements(),
                page.getTotalPages(), page.isLast()
        );
    }

    @Override
    public MessageResponse getProjectMessages(Long projectId, Integer size, Integer page_no) {
        Pageable pageable = PageRequest.of(page_no, size);
        Page<MessageProjection> page = repository.findByProjectProjectId(projectId, pageable);
        return new MessageResponse(
                page.toList(), page.getNumber(),
                page.getSize(), page.getTotalElements(),
                page.getTotalPages(), page.isLast()
        );
    }

    @Override
    public MessageResponse getModuleMessages(Long moduleId, Integer size, Integer page_no) {
        Pageable pageable = PageRequest.of(page_no, size);
        Page<MessageProjection> page = repository.findByModuleModuleId(moduleId, pageable);
        return new MessageResponse(
                page.toList(), page.getNumber(),
                page.getSize(), page.getTotalElements(),
                page.getTotalPages(), page.isLast()
        );
    }

    @Override
    public MessageResponse getTaskMessages(Long taskId, Integer size, Integer page_no) {
        Pageable pageable = PageRequest.of(page_no, size);
        Page<MessageProjection> page = repository.findByProjectProjectId(taskId, pageable);
        return new MessageResponse(
                page.toList(), page.getNumber(),
                page.getSize(), page.getTotalElements(),
                page.getTotalPages(), page.isLast()
        );
    }

    /**
     * Util methods
     */
    private String getTargetName(Long target) {
        return switch (target.intValue()) {
            case 1 -> "workspace";
            case 2 -> "project";
            case 3 -> "module";
            case 4 -> "task";
            default -> "unknown";
        };
    }

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

    private void attachMessageToTask(WorkspaceMessage message, Long targetId) {
        Task task = taskRepository.findById(targetId).orElseThrow(
                () -> new IllegalArgumentException("Task not found with ID : " + targetId)
        );
        message.setTask(task);
        taskRepository.save(task);
    }

    private void attachMessageToModule(WorkspaceMessage message, Long targetId) {
        ProjectModule module = moduleRepository.findById(targetId).orElseThrow(
                () -> new IllegalArgumentException("Module not found with ID : " + targetId)
        );
        message.setModule(module);
        moduleRepository.save(module);
    }

    private void attachMessageToProject(WorkspaceMessage message, Long targetId) {
        Project project = projectRepository.findById(targetId).orElseThrow(
                () -> new IllegalArgumentException("Project not found with ID : " + targetId)
        );
        message.setProject(project);
        projectRepository.save(project);
    }

    private void attachMessageToWorkspace(WorkspaceMessage message, Long targetId) {
        Workspace workspace = workspaceRepository.findById(targetId).orElseThrow(
                () -> new IllegalArgumentException("Workspace not found with ID : " + targetId)
        );
        message.setWorkspace(workspace);
        workspaceRepository.save(workspace);
    }

    private UserDTO getUserDto(User sender) {
        return new UserDTO(
                sender.getFullName(), sender.getEmail(), sender.getImage(), null
        );
    }
}
