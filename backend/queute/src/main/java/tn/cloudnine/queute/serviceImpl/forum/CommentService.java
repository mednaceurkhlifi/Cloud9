package tn.cloudnine.queute.serviceImpl.forum;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.forum.Comment;
import tn.cloudnine.queute.model.forum.Post;
import tn.cloudnine.queute.repository.forum.CommentRepository;
import tn.cloudnine.queute.repository.forum.PostRepository;
import tn.cloudnine.queute.service.forum.ICommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment create(Comment comment) {
        //TODO: add comment to the users list
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment) {
        //TODO: update comment to the users list
        Comment oldComment = findById(comment.getId());
        oldComment.setDate(comment.getDate());
        oldComment.setContent(comment.getContent());
        oldComment.setSentimentType(comment.getSentimentType());
        return commentRepository.save(oldComment);
    }

    @Override
    public Comment delete(long id) {
        //TODO: remove comment from the users list
        try{
            Comment comment = findById(id);
            commentRepository.deleteById(id);
            return comment;
        }catch (EntityNotFoundException e){
            return null;
        }
    }

    @Override
    public Comment findById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Comment not found with id :"+id));
        return comment;
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findCommentByPostId(postId).orElse(null);
    }
}
