package tn.cloudnine.queute.repository.serviceRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.ServiceAndFeedback.services.Services;
@Repository
public interface ServiceRepository extends JpaRepository<Services,Long> {
}
