package tn.cloudnine.queute.repository.serviceRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.ServiceAndFeedback.office.Office;

import java.util.List;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {
   // List<Office> findByOrganisation_id(Long organisationId);
   @Query("SELECT o FROM Office o WHERE o.organisation.organization_id = :organisationId")
   List<Office> findByOrganisationId(@Param("organisationId") Long organisationId);
}
