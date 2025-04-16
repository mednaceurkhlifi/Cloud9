package tn.cloudnine.queute.repository.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.cloudnine.queute.model.services.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
