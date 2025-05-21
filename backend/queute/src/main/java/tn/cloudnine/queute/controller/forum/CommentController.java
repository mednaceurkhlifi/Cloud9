package tn.cloudnine.queute.controller.forum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.dto.forum.CommentDTO;
import tn.cloudnine.queute.model.forum.Comment;
import tn.cloudnine.queute.service.forum.IFlaskService;
import tn.cloudnine.queute.serviceImpl.forum.CommentService;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("comment")
@CrossOrigin(origins = "*")

public class CommentController {
    private final CommentService commentService;
    private final IFlaskService flaskService;
    @PostMapping("create-comment")
    public ResponseEntity<Object> createComment(@RequestBody Comment comment) {
        if(flaskService.isToxic(comment)) {
            return ResponseEntity.badRequest().body("Failed to create due to toxicity");
        }
        comment.setDate(new Date());
        comment.setSentimentType(flaskService.sentimentAnalysis(comment));
        return ResponseEntity.ok().body(new CommentDTO(commentService.create(comment)));
    }
    @PutMapping("update-comment")
    public ResponseEntity<Object> updateComment(@RequestBody Comment comment) {
        if(flaskService.isToxic(comment)) {
            return ResponseEntity.badRequest().body("Failed to create due to toxicity");
        }
        comment.setSentimentType(flaskService.sentimentAnalysis(comment));
        return ResponseEntity.ok().body(new CommentDTO(commentService.update(comment)));
    }
    @DeleteMapping("delete-comment/{id}")
    public ResponseEntity<CommentDTO> deleteComment(@PathVariable long id) {
        //TODO: check for comment existence
        try{
            var comment = commentService.findById(id);
            commentService.delete(id);
            return ResponseEntity.ok().body(new CommentDTO(comment));

        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("get-comments/{id}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable Long id) {
        return ResponseEntity.ok().body(new CommentDTO(commentService.findById(id)));
    }
    @GetMapping("get-comments")
    public ResponseEntity<List<CommentDTO>> getComments() {
        var comments = commentService.findAll();

        return ResponseEntity.ok().body(comments.stream().map(e->new CommentDTO(e)).toList());
    }
    @GetMapping("get-comments/post/{id}")
    public ResponseEntity<List<CommentDTO>> getCommentsPerPost(@PathVariable Long id) {
        var comments = commentService.findByPostId(id);

        return ResponseEntity.ok().body(comments.stream().map(e->new CommentDTO(e)).toList());
    }
}
