package tn.cloudnine.queute.repository.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.cloudnine.queute.model.roadmap.FollowedRoadMap;

import java.util.List;

public interface FollowedRoadMapRepository extends JpaRepository<FollowedRoadMap,Long> {
    public List<FollowedRoadMap> findByUser_UserId(Long userId);
}
