package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.repository.workspace.ModuleRepository;
import tn.cloudnine.queute.service.workspace.IModuleService;

@Service
@RequiredArgsConstructor
public class ModuleService implements IModuleService {

    private final ModuleRepository repository;
}
