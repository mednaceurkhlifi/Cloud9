package tn.cloudnine.queute.controller.forum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.dto.forum.VoteDTO;
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
    public ResponseEntity<VoteDTO> createVote(@RequestBody Vote vote) {
        return ResponseEntity.ok().body(new VoteDTO(voteService.create(vote)));
    }
    @PutMapping("update-vote")
    public ResponseEntity<VoteDTO> updateVote(@RequestBody Vote vote) {
        return ResponseEntity.ok().body(new VoteDTO(voteService.update(vote)));
    }
    @DeleteMapping("delete-vote/{id}")
    public ResponseEntity<VoteDTO> deleteVote(@PathVariable long id) {
        try{
            var vote = voteService.findById(id);
            voteService.delete(id);
            return ResponseEntity.ok().body(new VoteDTO(vote));

        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("get-votes/{id}")
    public ResponseEntity<VoteDTO> getVote(@PathVariable Long id) {
        return ResponseEntity.ok().body(new VoteDTO(voteService.findById(id)));
    }
    @GetMapping("get-votes")
    public ResponseEntity<List<VoteDTO>> getVotes() {
        var votes = voteService.findAll();

        return ResponseEntity.ok().body(votes.stream().map(e->new VoteDTO(e)).toList());
    }
    @GetMapping("get-votes/post/{id}")
    public ResponseEntity<List<VoteDTO>> getVotesPerPost(@PathVariable Long id) {
        List<Vote> votes = postService.findById(id).getVotes();
        return ResponseEntity.ok().body(votes.stream().map(e->new VoteDTO(e)).toList());
    }
    @GetMapping("get-vote/user/{userId}/post/{postId}")
    public ResponseEntity<VoteDTO> getVotesPerUser(@PathVariable Long userId, @PathVariable Long postId) {
       Vote vote = voteService.findByUserIdAndPostId(userId,postId);
       return ResponseEntity.ok().body(new VoteDTO(vote));
    }
    @GetMapping("get-votes/comment/{id}")
    public ResponseEntity<List<VoteDTO>> getVotesPerComment(@PathVariable Long id) {
        List<Vote> votes = commentService.findById(id).getVotes();
        return ResponseEntity.ok().body(votes.stream().map(e->new VoteDTO(e)).toList());
    }
}
