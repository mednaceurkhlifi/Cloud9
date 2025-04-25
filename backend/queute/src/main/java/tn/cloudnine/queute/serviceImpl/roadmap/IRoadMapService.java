package tn.cloudnine.queute.serviceImpl.roadmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.roadmap.RoadMap;
import tn.cloudnine.queute.model.roadmap.RoadMapApproval;
import tn.cloudnine.queute.model.roadmap.Step;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.repository.roadmap.RoadMapRepository;
import tn.cloudnine.queute.service.roadmap.RoadMapService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IRoadMapService implements RoadMapService {

    private final RoadMapRepository roadMapRepository ;
    @Autowired
    public IRoadMapService(RoadMapRepository roadMapRepository){
        this.roadMapRepository=roadMapRepository;

    }

    @Override
    public void add(RoadMap roadMap) {
        this.roadMapRepository.save(roadMap);
    }

    @Override
    public void delete(Long id) {
        this.roadMapRepository.deleteById(id);
    }

    @Override
    public void update(Long id, RoadMap roadMap) {
        Optional<RoadMap> roadMap_opt = this.roadMapRepository.findById(id);
        if(roadMap_opt.isEmpty())
            throw new RuntimeException("can not find  Road map with this id");
        else {
            roadMap.setId(id);
            this.roadMapRepository.save(roadMap);
        }
    }

    @Override
    public Optional<RoadMap> findById(Long id) {
        return this.roadMapRepository.findById(id);
    }

    @Override
    public List<RoadMap> getAll() {
        return this.roadMapRepository.findAll();
    }

    @Override
    public boolean addStepToRoadMap(RoadMap roadMap, Step step){
//        if(!this.isOrderValid(step,roadMap))
//            return false ;
        if(roadMap.getSteps()==null)
            return false ;
        roadMap.getSteps().add(step) ;
        this.roadMapRepository.save(roadMap);
        return true;
    }
    @Override
    public boolean rmoveStepFromRoadMap(RoadMap roadMap,Step step){
        if(!this.isOrderValid(step,roadMap))
            return false ;
        roadMap.getSteps().remove(step);
        this.roadMapRepository.save(roadMap);
        return true;
    }
    public boolean isOrderValid(Step step, RoadMap roadMap){
        boolean valid= true;
        for (Step s:roadMap.getSteps()) {
            if((s.getStepOrder()==step.getStepOrder() ) && (step.isStrict())){
                valid =false;
                break;
            }
        }

        return valid;
    }

    public boolean approveRoadMap(Long id, boolean approved, User user){
        RoadMapApproval roadMapApproval = new RoadMapApproval();
        Optional<RoadMap>roadMap_opt = this.roadMapRepository.findById(id);
        if(roadMap_opt.isEmpty())
            return false;

        if(roadMap_opt.get().getApprovals().stream().anyMatch(e->e.getUser().getUserId()== user.getUserId())){
            return false ;
        }
        else{
            if(approved){
                roadMap_opt.get().setNbrApprove(roadMap_opt.get().getNbrApprove()+1);

            }else{
                roadMap_opt.get().setNbrDisapproval(roadMap_opt.get().getNbrDisapproval()+1);

            }

        }

        roadMapApproval.setApproved(approved);
        roadMapApproval.setDate(LocalDateTime.now());
        roadMap_opt.get().getApprovals().add(roadMapApproval);
        this.roadMapRepository.save(roadMap_opt.get());
        return true;
    }


}
