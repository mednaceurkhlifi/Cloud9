package tn.cloudnine.queute.controller.forum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.model.forum.Post;
import tn.cloudnine.queute.model.forum.Vote;
import tn.cloudnine.queute.service.forum.ICommentService;
import tn.cloudnine.queute.service.forum.IPostService;
import tn.cloudnine.queute.service.forum.IVoteService;
import tn.cloudnine.queute.service.user.IUserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("vote")
public class VoteController {

    public final IVoteService voteService;
    public final IPostService postService;
    public final ICommentService commentService;
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
        try{
            var vote = voteService.findById(id);
            voteService.delete(id);
            return ResponseEntity.ok().body(vote);

        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("get-votes/{id}")
    public ResponseEntity<Vote> getVote(@PathVariable Long id) {
        return ResponseEntity.ok().body(voteService.findById(id));
    }
    @GetMapping("get-votes")
    public ResponseEntity<List<Vote>> getVotes() {
        return ResponseEntity.ok().body(voteService.findAll());
    }
    @GetMapping("get-votes/post/{id}")
    public ResponseEntity<List<Vote>> getVotesPerPost(@PathVariable Long id) {
        List<Vote> votes = postService.findById(id).getVotes();
        return ResponseEntity.ok().body(votes);
    }
    @GetMapping("get-vote/user/{userId}/post/{postId}")
    public ResponseEntity<Vote> getVotesPerUser(@PathVariable Long userId, @PathVariable Long postId) {
       Vote vote = voteService.findByUserIdAndPostId(userId,postId);
       return ResponseEntity.ok().body(vote);
    }
    @GetMapping("get-votes/comment/{id}")
    public ResponseEntity<List<Vote>> getVotesPerComment(@PathVariable Long id) {
        List<Vote> votes = commentService.findById(id).getVotes();
        return ResponseEntity.ok().body(votes);
    }
}
