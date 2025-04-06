package tn.cloudnine.queute.controller.forum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.model.forum.Post;
import tn.cloudnine.queute.model.forum.Vote;
import tn.cloudnine.queute.service.forum.IPostService;
import tn.cloudnine.queute.service.forum.IVoteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("vote")
public class VoteController {

    public final IVoteService voteService;
    @PostMapping("create-vote")
    public ResponseEntity<Vote> createVote(@RequestBody Vote vote) {
        return ResponseEntity.ok().body(voteService.create(vote));
    }
    @PutMapping("update-vote")
    public ResponseEntity<Vote> updateVote(@RequestBody Vote vote) {
        return ResponseEntity.ok().body(voteService.update(vote));
    }
    @DeleteMapping("delete-vote/{id}")
    public ResponseEntity<Vote> deleteVote(@PathVariable long id) {
        //TODO: check for vote existence
        var vote = voteService.findById(id);
        if(vote == null){
            return ResponseEntity.notFound().build();
        }
        voteService.delete(id);
        return ResponseEntity.ok().body(vote);
    }
    @GetMapping("get-votes/{id}")
    public ResponseEntity<Vote> getVote(@PathVariable Long id) {
        return ResponseEntity.ok().body(voteService.findById(id));
    }
    @GetMapping("get-votes")
    public ResponseEntity<List<Vote>> getVotes() {
        return ResponseEntity.ok().body(voteService.findAll());
    }
}
