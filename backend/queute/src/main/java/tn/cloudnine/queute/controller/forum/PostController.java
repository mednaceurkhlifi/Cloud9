package tn.cloudnine.queute.controller.forum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.model.forum.Post;
import tn.cloudnine.queute.service.forum.IPostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("post")
public class PostController {
    public final IPostService postService;
    @PostMapping("create-post")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok().body(postService.create(post));
    }
    @PutMapping("update-post")
    public ResponseEntity<Post> updatePost(@RequestBody Post post) {
        return ResponseEntity.ok().body(postService.update(post));
    }
    @DeleteMapping("delete-post/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable long id) {
        //TODO: check for post existence
        try{
            var post = postService.findById(id);
            postService.delete(id);
            return ResponseEntity.ok().body(post);

        }catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("get-posts/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        return ResponseEntity.ok().body(postService.findById(id));
    }
    @GetMapping("get-posts")
    public ResponseEntity<List<Post>> getPosts() {
        return ResponseEntity.ok().body(postService.findAll());
    }
}
