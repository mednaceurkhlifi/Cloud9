package tn.cloudnine.queute.controller.roadmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import tn.cloudnine.queute.model.roadmap.FollowedRoadMap;
import tn.cloudnine.queute.model.roadmap.RoadMap;
import tn.cloudnine.queute.model.roadmap.StepProgress;
import tn.cloudnine.queute.repository.roadmap.FollowedRoadMapRepository;
import tn.cloudnine.queute.repository.roadmap.StepProgressRepository;
import tn.cloudnine.queute.service.roadmap.FollowedRoadMapService;
import tn.cloudnine.queute.service.roadmap.StepProgressService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("followed-road-map")
public class FollowedRoadMapController {
    private final FollowedRoadMapService followedRoadMapService;
    private final StepProgressService stepProgressService;
    private final HandlerMapping resourceHandlerMapping;

    @Autowired
    public FollowedRoadMapController(FollowedRoadMapService followedRoadMapService, StepProgressService stepProgressService, HandlerMapping resourceHandlerMapping) {
        this.followedRoadMapService = followedRoadMapService;
        this.stepProgressService = stepProgressService;
        this.resourceHandlerMapping = resourceHandlerMapping;
    }
    @GetMapping("get-all")
    public ResponseEntity<List<FollowedRoadMap>> getAll() {
        return  ResponseEntity.ok(followedRoadMapService.getAll());
    }
    @GetMapping("find/{id}")
    public ResponseEntity<FollowedRoadMap> findById(@PathVariable Long id) {
        Optional<FollowedRoadMap> FollowedRoadMap_opt = this.followedRoadMapService.findById(id);
        return FollowedRoadMap_opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/get-by-user-id/{id}")
    public ResponseEntity<List<FollowedRoadMap>> getByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(followedRoadMapService.getByUserId(id));
    }
    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody FollowedRoadMap followedRoadMap) {
        if(this.followedRoadMapService.add(followedRoadMap))
        return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<FollowedRoadMap> FollowedRoadMap_opt = this.followedRoadMapService.findById(id);
        if(FollowedRoadMap_opt.isEmpty())
            return ResponseEntity.notFound().build();
        else {
            this.followedRoadMapService.delete(id);
            return ResponseEntity.ok().build();
        }
    }
    @PutMapping("change-step-status/{id}")
    public ResponseEntity<?> changeStepStatus(@PathVariable("id") Long id,@RequestParam("stepId")Long stepId) {
        Optional<FollowedRoadMap>followedRoadMap_opt = this.followedRoadMapService.findById(id);
        Optional<StepProgress> stepProgress_opt ;
        if(followedRoadMap_opt.isEmpty())
            return ResponseEntity.notFound().build();
        else{
            stepProgress_opt = this.stepProgressService.findById(stepId);
            if (stepProgress_opt.isPresent()) {
                this.stepProgressService.updateStatus(stepId);
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.notFound().build();
            }
        }
    }
    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody FollowedRoadMap followedRoadMap) {
        this.followedRoadMapService.update(id, followedRoadMap);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("unfollow/{id}")
    public ResponseEntity<?> unfollow(@PathVariable Long id) {
        this.followedRoadMapService.delete(id);
        return ResponseEntity.ok().build();
    }


}
