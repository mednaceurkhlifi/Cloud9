package tn.cloudnine.queute.serviceImpl.forum;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.dto.forum.PostCountDTO;
import tn.cloudnine.queute.enums.forum.SentimentType;
import tn.cloudnine.queute.model.forum.Post;
import tn.cloudnine.queute.repository.forum.PostRepository;
import tn.cloudnine.queute.service.forum.IPostService;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;

    @Override
    public Post create(Post post) {
        //TODO: add post to the users list
        return postRepository.save(post);
    }

    @Override
    public Post update(Post post) {
        //TODO: update post to the users list
        Post oldPost = findById(post.getId());
        oldPost.setTitle(post.getTitle());
        oldPost.setContent(post.getContent());
        oldPost.setDate(post.getDate());
        oldPost.setImage(post.getImage());
        oldPost.setSentimentType(post.getSentimentType());
        return postRepository.save(oldPost);
    }

    @Override
    public Post delete(long id) {
        //TODO: remove post from the users list
        try{
            Post post = findById(id);
            postRepository.deleteById(id);
            return post;
        }catch (EntityNotFoundException e){
            //TODO: do some abt this
            return null;
        }
    }

    @Override
    public Post findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Post not found with id :"+id));
        return post;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }
    public List<Post> findBySentimentType(SentimentType sentimentType) {
        return postRepository.getPostsBySentimentType(sentimentType);
    }

    @Override
    public List<PostCountDTO> countPostsByDate() {
        List<Post> posts = postRepository.findAll();
        Map<String, Long> monthlyCounts = posts.stream()
                .collect(Collectors.groupingBy(
                        post -> post.getDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.counting()
                ));
        List<PostCountDTO> monthlyPostCounts = monthlyCounts.entrySet().stream()
                .map(entry -> new PostCountDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return monthlyPostCounts;
    }

}
