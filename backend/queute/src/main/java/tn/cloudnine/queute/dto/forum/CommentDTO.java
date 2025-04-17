package tn.cloudnine.queute.dto.forum;

import tn.cloudnine.queute.model.forum.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private List<VoteDTO> votes;
    CommentDTO(Comment comment){
        this.id=comment.getId();
        this.postId=comment.getPost().getId();
        this.userId=comment.getUser().getUser_id();
        this.content=comment.getContent();
        this.votes=new ArrayList<>();
        comment.getVotes().forEach(c->this.votes.add(new VoteDTO(c)));
    }
}
