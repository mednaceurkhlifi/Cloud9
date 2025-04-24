package tn.cloudnine.queute.controller.forum;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.forum.PostCountDTO;
import tn.cloudnine.queute.dto.forum.PostDTO;
import tn.cloudnine.queute.enums.forum.SentimentType;
import tn.cloudnine.queute.model.forum.ImageEntity;
import tn.cloudnine.queute.model.forum.Post;
import tn.cloudnine.queute.service.forum.IFlaskService;
import tn.cloudnine.queute.service.forum.IPostService;
import tn.cloudnine.queute.utils.FileUploaderImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("post")
public class PostController {
    public final IPostService postService;
    public final FileUploaderImpl fu;
    public final IFlaskService flaskService;
    @PostMapping(value="create-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createPost(@RequestPart("post") Post post, @RequestPart(value="file",required = false)MultipartFile file) throws IOException {
        if(flaskService.isToxic(post)) {
            return ResponseEntity.badRequest().body("Failed to create due to toxicity");
        }
        if(file!=null){
            String fileName = fu.saveImage(file);
            ImageEntity image = new ImageEntity();
            image.setName(file.getOriginalFilename());
            image.setUrl("/uploads/images/"+fileName);
            post.setImage(image);
        }
        post.setSentimentType(flaskService.sentimentAnalysis(post));
        post.setDate(new Date());
        return ResponseEntity.ok().body(new PostDTO(postService.create(post)));
    }
    @PutMapping(value="update-post",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updatePost(@RequestPart("post") Post post,@RequestPart(value="file",required = false) MultipartFile file) throws IOException {
        if(flaskService.isToxic(post)) {
            return ResponseEntity.badRequest().body("Failed to update due to toxicity");
        }
        if(file!=null){
            String fileName = fu.saveImage(file);
            ImageEntity image = new ImageEntity();
            image.setName(file.getOriginalFilename());
            image.setUrl("/uploads/images/"+fileName);
            if(post.getImage()!=null){
                fu.deleteFile(post.getImage().getUrl());
            }
            post.setImage(image);
        }
        post.setSentimentType(flaskService.sentimentAnalysis(post));
        return ResponseEntity.ok().body(new PostDTO(postService.update(post)));
    }
    @DeleteMapping("delete-post/{id}")
    public ResponseEntity<PostDTO> deletePost(@PathVariable long id) {
        //TODO: check for post existence
        try{
            var post = postService.findById(id);
            if(post.getImage()!=null){
                fu.deleteFile(post.getImage().getUrl());
            }
            postService.delete(id);
            return ResponseEntity.ok().body(new PostDTO(post));

        }catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("get-posts/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
        return ResponseEntity.ok().body(new PostDTO(postService.findById(id)));
    }
    @GetMapping("get-posts")
    public ResponseEntity<List<PostDTO>> getPosts() {
        var posts = postService.findAll();

        return ResponseEntity.ok().body(posts.stream().map(e-> new PostDTO(e)).toList());
    }
    @GetMapping("get-posts/summary/{id}")
    public ResponseEntity<String> getPostSummary(@PathVariable Long id) {
        Post post=postService.findById(id);

        return ResponseEntity.ok().body(flaskService.Summarizer(post));
    }

    @GetMapping("stats/positive-posts")
    public ResponseEntity<List<PostDTO>> getPostivePosts() {
        return ResponseEntity.ok().body(postService.findBySentimentType(SentimentType.POSITIVE).stream().map(e -> new PostDTO(e)).toList());
    }

    @GetMapping("stats/negative-posts")
    public ResponseEntity<List<PostDTO>> getNegativePosts() {
        return ResponseEntity.ok().body(postService.findBySentimentType(SentimentType.NEGATIVE).stream().map(e -> new PostDTO(e)).toList());
    }

    @GetMapping("stats/neutral-posts")
    public ResponseEntity<List<PostDTO>> getNeutralPosts() {
        return ResponseEntity.ok().body(postService.findBySentimentType(SentimentType.NEUTRAL).stream().map(e -> new PostDTO(e)).toList());
    }
    @GetMapping("stats/posts-over-time")
    public ResponseEntity<List<PostCountDTO>> getPostCountsOverTime() {
        return ResponseEntity.ok(postService.countPostsByDate());
    }
}
