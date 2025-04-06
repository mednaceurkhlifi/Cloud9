package tn.cloudnine.queute.service.forum;

import tn.cloudnine.queute.model.forum.Post;

import java.util.List;

public interface IPostService {
    public Post create(Post post);
    public Post update(Post post);
    public Post delete(long id);
    public Post findById(Long id);
    public List<Post> findAll();
}
