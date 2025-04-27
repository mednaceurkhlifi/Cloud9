package tn.cloudnine.queute.serviceImpl.roadmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.roadmap.FollowedRoadMap;
import tn.cloudnine.queute.model.roadmap.RoadMap;
import tn.cloudnine.queute.model.roadmap.Step;
import tn.cloudnine.queute.model.roadmap.StepProgress;
import tn.cloudnine.queute.repository.roadmap.FollowedRoadMapRepository;
import tn.cloudnine.queute.repository.roadmap.RoadMapRepository;
import tn.cloudnine.queute.service.roadmap.FollowedRoadMapService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IFollowedRoadMapService implements FollowedRoadMapService {

    private final FollowedRoadMapRepository followedRoadMapRepository;
    private final RoadMapRepository roadMapRepository;
    @Autowired
    public IFollowedRoadMapService(FollowedRoadMapRepository followedRoadMapRepository,RoadMapRepository roadMapRepository) {
        this.followedRoadMapRepository = followedRoadMapRepository;
        this.roadMapRepository = roadMapRepository;
    }

    @Override
    public boolean add(FollowedRoadMap followedRoadMap) {
        Optional<RoadMap>roadMap_opt ;
        if(followedRoadMap.getRoadMapId()!=null)
        roadMap_opt =this.roadMapRepository.findById(followedRoadMap.getRoadMapId());
        else return false;

        if(roadMap_opt.isPresent()) {
            if (!roadMap_opt.get().getSteps().isEmpty()) {
                followedRoadMap.setFrTitle(roadMap_opt.get().getTitle());
                followedRoadMap.setFrDescription(roadMap_opt.get().getDescription());
                for (Step step : roadMap_opt.get().getSteps()) {
                    StepProgress stepProgress = new StepProgress();
                    stepProgress.setStepId(step.getId());
                    stepProgress.setSpDescription(step.getStepDescription());
                    stepProgress.setSpRequiredPaper(step.getRequiredPapers());
                    followedRoadMap.getStepProgressList().add(stepProgress);
                }
            }

        }
       this.followedRoadMapRepository.save(followedRoadMap);
        return true;
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
    @Override
    public  List<FollowedRoadMap> getByUserId(Long userId) {
        return this.followedRoadMapRepository.findByUser_UserId(userId);
    }

}
