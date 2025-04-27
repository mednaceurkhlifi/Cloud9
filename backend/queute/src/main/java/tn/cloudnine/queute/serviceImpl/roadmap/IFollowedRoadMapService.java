package tn.cloudnine.queute.serviceImpl.roadmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.roadmap.FollowedRoadMap;
import tn.cloudnine.queute.repository.roadmap.FollowedRoadMapRepository;
import tn.cloudnine.queute.service.roadmap.FollowedRoadMapService;

import java.util.List;
import java.util.Optional;

@Service
public class IFollowedRoadMapService implements FollowedRoadMapService {

    private final FollowedRoadMapRepository followedRoadMapRepository;
    @Autowired
    public IFollowedRoadMapService(FollowedRoadMapRepository followedRoadMapRepository) {
        this.followedRoadMapRepository = followedRoadMapRepository;
    }

    @Override
    public void add(FollowedRoadMap folowedRoadMap) {
        if(folowedRoadMap.getRoadMap().getSteps().size()>0)
       this.followedRoadMapRepository.save(folowedRoadMap);
    }

    @Override
    public void delete(Long id) {
    this.followedRoadMapRepository.deleteById(id);
    }

    @Override
    public void update(Long id, FollowedRoadMap folowedRoadMap) {
    this.followedRoadMapRepository.save(folowedRoadMap);
    }

    @Override
    public Optional<FollowedRoadMap> findById(Long id) {
        return this.followedRoadMapRepository.findById(id);
    }

    @Override
    public List<FollowedRoadMap> getAll() {
        return this.followedRoadMapRepository.findAll();
    }

}
