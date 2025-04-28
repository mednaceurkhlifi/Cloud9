package tn.cloudnine.queute.controller.roadmap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.model.roadmap.RoadMap;
import tn.cloudnine.queute.model.roadmap.RoadMapCreatorScore;
import tn.cloudnine.queute.model.roadmap.Step;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.service.roadmap.RoadMapService;
import tn.cloudnine.queute.service.roadmap.StepService;
import tn.cloudnine.queute.serviceImpl.roadmap.RoadMapGeminiService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("road-map")
public class RoadMapController {

    private final RoadMapService roadMapService ;
    private final RoadMapGeminiService roadMapGeminiService ;
    @Autowired
    public  RoadMapController (RoadMapService roadMapService,
                               RoadMapGeminiService roadMapGeminiServic){
        this.roadMapService=roadMapService;
        this.roadMapGeminiService=roadMapGeminiServic;
    }

//    @GetMapping("/gemini")
//    public ResponseEntity<String>  testGemini(@RequestBody RoadMap roadMap) {
//        return ResponseEntity.ok(roadMapGeminiService.clarifyRoadmapJson(roadMap));
//    }
//    @GetMapping("/gemini")
//    public ResponseEntity<String> clarifyRoadmap(@RequestBody RoadMap roadMap) {
//        try {
//            String clarifiedJson = roadMapGeminiService.clarifyText(roadMap.getDescription());
//            return ResponseEntity.ok(clarifiedJson);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error processing roadmap: " + e.getMessage());
//        }
//    }


    @PostMapping("/clarify-texts")
    public ResponseEntity<RoadMap> clarifyRoadMapTexts(@RequestBody RoadMap roadMap) {
        try {
            RoadMap clarified = roadMapService.clarifyRoadMapTexts(roadMap);
            return ResponseEntity.ok(clarified);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("get-all")
    public ResponseEntity<List<RoadMap>> getAll(){
        return  ResponseEntity.ok(this.roadMapService.getAll());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<RoadMap> getById(@PathVariable("id") Long id){
        Optional<RoadMap> roadMap_opt = this.roadMapService.findById(id);
        if(roadMap_opt.isEmpty())
            return ResponseEntity.notFound().build();
        else{
            return ResponseEntity.ok(roadMap_opt.get());
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")Long id){
        Optional<RoadMap> roadMap_opt = this.roadMapService.findById(id);
        if(roadMap_opt.isEmpty())
            return ResponseEntity.notFound().build();
        else{
            this.roadMapService.delete(id);
            return ResponseEntity.ok().body(Map.of("message","Successfully deleted"));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody RoadMap roadMap,
                                 @RequestParam(required = false, defaultValue = "false") boolean enhance){

        if (enhance) {

            roadMap = roadMapService.clarifyRoadMapTexts(roadMap);
        }

        this.roadMapService.add(roadMap);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "status","success" ,
                "data",roadMap
        ));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,@RequestBody RoadMap roadMap){
        Optional<RoadMap> roadMap_opt = this.roadMapService.findById(id);
        if(roadMap_opt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        else {
            this.roadMapService.update(id, roadMap);
            return ResponseEntity.ok().body(Map.of(
                    "status","success",
                    "data",roadMap
            ));
        }

    }

    @PutMapping("/affect-step-to-road-map/{id}")
    public ResponseEntity<?>affectStepToRoadMap(@PathVariable("id")Long id,
                                                @RequestBody Step step){
        if(id==null)
            return  ResponseEntity.badRequest().body("id road map null is"+id);

        Optional<RoadMap> roadMap_opt =this.roadMapService.findById(id);
        if(roadMap_opt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("road map not found "+id);
        else {
            this.roadMapService.addStepToRoadMap(roadMap_opt.get(),step);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
//        //return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/remove-step-from-road-map/{id-road-map}")
    public ResponseEntity<?> romoveStepFromRoadMap(@PathVariable("id-road-map")Long idRoadMap,
                                                   @RequestBody Step step){
        if(idRoadMap==null)
            return  ResponseEntity.badRequest().body("id is null");
        Optional<RoadMap> roadMap_opt =this.roadMapService.findById(idRoadMap);
        if(roadMap_opt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("road map not found ");
        else {
            this.roadMapService.rmoveStepFromRoadMap(roadMap_opt.get(),step);
            return ResponseEntity.status(HttpStatus.OK).build();
        }

    }
    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable("id") Long id,@RequestBody User user){
        if(this.roadMapService.approveRoadMap(id,true,user))
            return ResponseEntity.status(HttpStatus.OK).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PutMapping("/disapprove/{id}")
    public ResponseEntity<?> disapprove(@PathVariable("id") Long id,@RequestBody User user){
        if(this.roadMapService.approveRoadMap(id,false,user))
            return ResponseEntity.status(HttpStatus.OK).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @GetMapping("scores")
    public ResponseEntity<List<RoadMapCreatorScore>> getScores(){
        return ResponseEntity.ok(this.roadMapService.getScores());
    }

}
