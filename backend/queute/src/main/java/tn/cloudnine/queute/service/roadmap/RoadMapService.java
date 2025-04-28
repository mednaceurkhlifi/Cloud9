package tn.cloudnine.queute.service.roadmap;

import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.roadmap.RoadMap;
import tn.cloudnine.queute.model.roadmap.RoadMapCreatorScore;
import tn.cloudnine.queute.model.roadmap.Step;
import tn.cloudnine.queute.model.user.User;

import java.util.List;
import java.util.Optional;
@Service
public interface RoadMapService {
    public void add(RoadMap roadMap);
    public void delete(Long id);
    public void update(Long id,RoadMap roadMap) ;
    public Optional<RoadMap> findById(Long id);
    public List<RoadMap> getAll();
    public boolean addStepToRoadMap(RoadMap roadMap, Step step);
    public boolean rmoveStepFromRoadMap(RoadMap roadMap,Step step);
    public boolean approveRoadMap(Long id, boolean isApproved, User user);
    public RoadMap clarifyRoadMapTexts(RoadMap roadMap);
    public List<RoadMapCreatorScore> getScores();
}
