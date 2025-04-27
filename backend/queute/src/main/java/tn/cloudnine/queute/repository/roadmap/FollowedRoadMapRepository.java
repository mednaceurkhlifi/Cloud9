package tn.cloudnine.queute.repository.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.cloudnine.queute.model.roadmap.FollowedRoadMap;

public interface FollowedRoadMapRepository extends JpaRepository<FollowedRoadMap,Long> {
}
