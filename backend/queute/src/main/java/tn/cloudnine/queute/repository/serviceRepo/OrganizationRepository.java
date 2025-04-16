package tn.cloudnine.queute.repository.serviceRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
