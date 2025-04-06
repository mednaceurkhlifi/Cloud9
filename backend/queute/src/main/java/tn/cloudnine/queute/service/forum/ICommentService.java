package tn.cloudnine.queute.service.forum;

import tn.cloudnine.queute.model.forum.Comment;
import tn.cloudnine.queute.model.forum.Post;

import java.util.List;

public interface ICommentService {

    public Comment create(Comment comment);
    public Comment update(Comment comment);
    public Comment delete(long id);
    public Comment findById(Long id);
    public List<Comment> findAll();
}
