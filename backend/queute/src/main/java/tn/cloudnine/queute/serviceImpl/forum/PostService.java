package tn.cloudnine.queute.serviceImpl.forum;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.forum.Post;
import tn.cloudnine.queute.repository.forum.PostRepository;
import tn.cloudnine.queute.service.forum.IPostService;

import java.util.List;

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
        return postRepository.save(post);
    }

    @Override
    public void delete(Post post) {
        //TODO: remove post from the users list
         postRepository.delete(post);
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
}
