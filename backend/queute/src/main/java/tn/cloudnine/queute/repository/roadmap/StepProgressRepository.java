package tn.cloudnine.queute.repository.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.cloudnine.queute.model.roadmap.StepProgress;

public interface StepProgressRepository extends JpaRepository<StepProgress,Long> {
}
