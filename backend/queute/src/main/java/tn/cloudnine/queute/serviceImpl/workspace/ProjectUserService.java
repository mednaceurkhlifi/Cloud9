package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.repository.workspace.ProjectUserRepository;
import tn.cloudnine.queute.service.workspace.IProjectUserService;

@Service
@RequiredArgsConstructor
public class ProjectUserService implements IProjectUserService {

    private ProjectUserRepository repository;
}
