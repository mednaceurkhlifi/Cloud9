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


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class IRoadMapService implements RoadMapService {

    private final RoadMapRepository roadMapRepository ;
    private final RoadMapGeminiService textClarifier ;
    @Autowired
    public IRoadMapService(RoadMapRepository roadMapRepository,RoadMapGeminiService textClarifier){
        this.roadMapRepository=roadMapRepository;
        this.textClarifier=textClarifier;

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
        System.out.println(user);
        Optional<RoadMap>roadMap_opt = this.roadMapRepository.findById(id);
        System.out.println(roadMap_opt.get());
        if(roadMap_opt.isEmpty())
            return false;

        if(roadMap_opt.get().getApprovals().stream().anyMatch(e->e.getUser().getUserId() == user.getUserId())){

            return false ;
        }
        else{
            if(approved){
                roadMap_opt.get().setNbrApprove(roadMap_opt.get().getNbrApprove()+1);

            }else{
                roadMap_opt.get().setNbrDisapproval(roadMap_opt.get().getNbrDisapproval()+1);

            }

        }
        roadMapApproval.setUser(user);
        roadMapApproval.setApproved(approved);
        roadMapApproval.setDate(LocalDateTime.now());
        System.out.println(roadMapApproval);
        roadMap_opt.get().getApprovals().add(roadMapApproval);

        this.roadMapRepository.save(roadMap_opt.get());
        return true;
    }

    @Override
    public RoadMap clarifyRoadMapTexts(RoadMap roadMap) {
        try {
            // Process main fields
            roadMap.setTitle(textClarifier.clarifyText(roadMap.getTitle()));
            roadMap.setDescription(textClarifier.clarifyText(roadMap.getDescription()));

            // Process steps
            if (roadMap.getSteps() != null) {
                for (Step step : roadMap.getSteps()) {
                    step.setStepName(textClarifier.clarifyText(step.getStepName()));
                    step.setStepDescription(textClarifier.clarifyText(step.getStepDescription()));
                    step.setRequiredPapers(textClarifier.clarifyText(step.getRequiredPapers()));
                }
            }

            return roadMap;
        } catch (Exception e) {
            throw new RuntimeException("Failed to clarify roadmap texts", e);
        }
    }








}
