package tn.cloudnine.queute.serviceImpl.roadmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.roadmap.*;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.repository.roadmap.FollowedRoadMapRepository;
import tn.cloudnine.queute.repository.roadmap.RoadMapRepository;
import tn.cloudnine.queute.service.roadmap.RoadMapService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;

@Service
public class IRoadMapService implements RoadMapService {

    private final RoadMapRepository roadMapRepository ;
    private final RoadMapGeminiService textClarifier ;
    private final FollowedRoadMapRepository followedRoadMapRepository ;
    @Autowired
    public IRoadMapService(FollowedRoadMapRepository followedRoadMapRepository,RoadMapRepository roadMapRepository,RoadMapGeminiService textClarifier){
        this.roadMapRepository=roadMapRepository;
        this.textClarifier=textClarifier;
        this.followedRoadMapRepository=followedRoadMapRepository;

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
    public List<RoadMapCreatorScore> getScores() {

        List<RoadMap> allRoadMaps = roadMapRepository.findAll();

        List<FollowedRoadMap> allFollowedRoadMaps = followedRoadMapRepository.findAll();


        Map<User, RoadMapCreatorScore> creatorScoresMap = new HashMap<>();


        for (RoadMap roadMap : allRoadMaps) {
            User creator = roadMap.getCreator();

            RoadMapCreatorScore score = creatorScoresMap.computeIfAbsent(creator,
                    k -> new RoadMapCreatorScore(creator, 0, 0, 0, 0L));


            score.setTotlaNbrRoadMap(score.getTotlaNbrRoadMap() + 1);


            score.setTotaleNbrApprovalls(score.getTotaleNbrApprovalls() + roadMap.getNbrApprove());
        }


        for (FollowedRoadMap followed : allFollowedRoadMaps) {

            RoadMap originalRoadMap = roadMapRepository.findById(followed.getRoadMapId()).orElse(null);
            if (originalRoadMap != null) {
                User creator = originalRoadMap.getCreator();

                RoadMapCreatorScore score = creatorScoresMap.computeIfAbsent(creator,
                        k -> new RoadMapCreatorScore(creator, 0, 0, 0, 0L));


                score.setTotaleNbrFollow(score.getTotaleNbrFollow() + 1);
            }
        }


        List<RoadMapCreatorScore> result = creatorScoresMap.values().stream()
                .peek(score -> {

                    long calculatedScore = score.getTotlaNbrRoadMap() * 10L
                            + score.getTotaleNbrApprovalls() * 5L
                            + score.getTotaleNbrFollow() * 2L;
                    score.setScore(calculatedScore);
                }).sorted((s1, s2) -> Long.compare(s2.getScore(), s1.getScore()))
                .collect(Collectors.toList());

        return result;
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
