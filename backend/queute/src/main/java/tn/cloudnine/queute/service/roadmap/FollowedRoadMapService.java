package tn.cloudnine.queute.service.roadmap;

import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.roadmap.FollowedRoadMap;

import java.util.List;
import java.util.Optional;

@Service
public interface FollowedRoadMapService {
    public void add(FollowedRoadMap folowedRoadMap);
    public void delete(Long id);
    public void update(Long id, FollowedRoadMap folowedRoadMap) ;
    public Optional<FollowedRoadMap> findById(Long id);
    public List<FollowedRoadMap> getAll();
}
