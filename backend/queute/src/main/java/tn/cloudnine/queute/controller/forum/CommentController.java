package tn.cloudnine.queute.controller.forum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.dto.forum.CommentDTO;
import tn.cloudnine.queute.model.forum.Comment;
import tn.cloudnine.queute.serviceImpl.forum.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("create-comment")
    public ResponseEntity<CommentDTO> createComment(@RequestBody Comment comment) {
        return ResponseEntity.ok().body(new CommentDTO(commentService.create(comment)));
    }
    @PutMapping("update-comment")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody Comment comment) {
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
