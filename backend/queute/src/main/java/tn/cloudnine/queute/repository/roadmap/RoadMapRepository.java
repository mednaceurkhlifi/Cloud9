package tn.cloudnine.queute.repository.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.cloudnine.queute.model.roadmap.RoadMap;

public interface RoadMapRepository  extends JpaRepository<RoadMap, Long> {
}
