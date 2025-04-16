package tn.cloudnine.queute.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import tn.cloudnine.queute.model.ServiceAndFeedback.office.Office;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.Organization;
import tn.cloudnine.queute.repository.serviceRepo.OfficeRepository;
import tn.cloudnine.queute.services.IOfficeService;
import tn.cloudnine.queute.repository.serviceRepo.OrganizationRepository;

import java.util.List;

@Service

public class OfficeService implements IOfficeService {
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private OrganizationRepository orgRepo;

    public OfficeService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    /*public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

     */
    public List<Office> getAllOfficesByOrganisationId(Long organisationId) {
        return officeRepository.findByOrganisationId(organisationId);
    }

    public Office getOfficeById(Long id) {
        return officeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Office not found"));
    }

    public Office createOffice(Long organisationId, Office office) {
        Organization organisation = orgRepo.findById(organisationId).orElse(null);
        if (organisation != null) {
            office.setOrganisation(organisation);
            return officeRepository.save(office);
        }
        return null;
    }


    public Office updateOffice(Long id, Office updatedOffice) {
        Office existing = getOfficeById(id);
        existing.setName(updatedOffice.getName());
        existing.setLocation(updatedOffice.getLocation());
        existing.setPhoneNumber(updatedOffice.getPhoneNumber());
        existing.setOrganisation(updatedOffice.getOrganisation());
        return officeRepository.save(existing);
    }

    public void deleteOffice(Long id) {
        officeRepository.deleteById(id);
    }

    @Override
    public Office addOfficeAndAssignToOrganisation(Long organisationId, Office office) {
        Organization organisation = orgRepo.findById(organisationId)
                .orElseThrow(() -> new ResourceAccessException(
                        "Organisation non trouvée ou déjà supprimée avec l'ID: " + organisationId));



        office.setOrganisation(organisation);
        return officeRepository.save(office);
    }
}
