package tn.cloudnine.queute.services;

import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.Organization;

import java.util.List;
import java.util.Optional;

public interface IOrganisationService {
    List<Organization> findAll();
    Optional<Organization> findById(Long id);
    Organization createOrganization(Organization organisation, MultipartFile imageFile, String user_email);
    Organization updateOrganisation(Long id, Organization organisation, MultipartFile imageFile);


    public boolean existsById(Long id);
}
