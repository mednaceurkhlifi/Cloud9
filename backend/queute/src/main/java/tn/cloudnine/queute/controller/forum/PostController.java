package tn.cloudnine.queute.controller.forum;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.forum.PostDTO;
import tn.cloudnine.queute.model.forum.ImageEntity;
import tn.cloudnine.queute.model.forum.Post;
import tn.cloudnine.queute.service.forum.IPostService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("post")
public class PostController {
    public final IPostService postService;
    @PostMapping(value="create-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDTO> createPost(@RequestPart("post") Post post, @RequestPart(value="file",required = false)MultipartFile file) throws IOException {
        try{
            if(file!=null){
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                String uploadsDir = Paths.get("").toAbsolutePath().toString() + File.separator +"uploads\\images";
                Files.createDirectories(Paths.get(uploadsDir));
                String filePath =uploadsDir+File.separator + fileName;
                file.transferTo(new File(filePath));
                ImageEntity image = new ImageEntity();
                image.setName(file.getOriginalFilename());
                image.setUrl("/uploads/images/"+fileName);
                post.setImage(image);
            }
            post.setDate(new Date());
            return ResponseEntity.ok().body(new PostDTO(postService.create(post)));
        }catch(IOException ex){
            System.err.println(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    @PutMapping("update-post")
    public ResponseEntity<PostDTO> updatePost(@RequestBody Post post) {
        return ResponseEntity.ok().body(new PostDTO(postService.update(post)));
    }
    @DeleteMapping("delete-post/{id}")
    public ResponseEntity<PostDTO> deletePost(@PathVariable long id) {
        //TODO: check for post existence
        try{
            var post = postService.findById(id);
            if(post.getImage()!=null){
                try{
                    String uploadsDir = Paths.get("").toAbsolutePath().toString() + post.getImage().getUrl();
                    Files.deleteIfExists(Paths.get(uploadsDir));

                }catch (IOException e){
                    System.err.println("file does not exist");
                }
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
}
