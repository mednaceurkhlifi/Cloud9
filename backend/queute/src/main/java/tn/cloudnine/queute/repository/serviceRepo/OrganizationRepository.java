package tn.cloudnine.queute.repository.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.organization.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
