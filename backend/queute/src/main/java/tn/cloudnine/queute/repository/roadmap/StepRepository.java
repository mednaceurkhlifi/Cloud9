package tn.cloudnine.queute.repository.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.cloudnine.queute.model.roadmap.Step;

public interface StepRepository extends JpaRepository<Step, Long> {
}
