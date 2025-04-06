package tn.cloudnine.queute.controller.forum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.model.forum.Comment;
import tn.cloudnine.queute.serviceImpl.forum.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("create-comment")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        return ResponseEntity.ok().body(commentService.create(comment));
    }
    @PutMapping("update-comment")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
        return ResponseEntity.ok().body(commentService.update(comment));
    }
    @DeleteMapping("delete-comment/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable long id) {
        //TODO: check for comment existence
        commentService.delete(id);
        var comment = commentService.findById(id);
        return ResponseEntity.ok().body(comment);
    }
    @GetMapping("get-comments/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        return ResponseEntity.ok().body(commentService.findById(id));
    }
    @GetMapping("get-comments")
    public ResponseEntity<List<Comment>> getComments() {
        return ResponseEntity.ok().body(commentService.findAll());
    }
}
