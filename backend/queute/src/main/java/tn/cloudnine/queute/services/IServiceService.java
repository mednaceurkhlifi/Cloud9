package tn.cloudnine.queute.services;

import tn.cloudnine.queute.model.ServiceAndFeedback.services.Services;

import java.util.List;
import java.util.Optional;

public interface IServiceService {
    List<Services> findAll();
    Optional<Services> findById(Long id);
    Services createServiceWithOfficeId(Services service, Long officeId);

    void deleteById(Long id);
}
