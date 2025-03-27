package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.repository.workspace.ProjectDocumentRepository;
import tn.cloudnine.queute.service.workspace.IProjectDocumentService;

@Service
@RequiredArgsConstructor
public class ProjectDocumentService implements IProjectDocumentService {

    private final ProjectDocumentRepository repository;
}
