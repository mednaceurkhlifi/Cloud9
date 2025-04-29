package tn.cloudnine.queute.services;

import tn.cloudnine.queute.model.ServiceAndFeedback.office.Office;

import java.util.List;

public interface IOfficeService {
   // List<Office> getAllOffices();
    Office getOfficeById(Long id);
   // Office createOffice(Long organisationId, Office office) ;
    Office updateOffice(Long id, Office office);
    void deleteOffice(Long id);
    Office addOfficeAndAssignToOrganisation(Long organisationId, Office office);
    List<Office> getAllOfficesByOrganisationId(Long organisationId);
}
