package tn.cloudnine.queute.dto.forum;

import lombok.Data;
import tn.cloudnine.queute.enums.forum.SentimentType;
import tn.cloudnine.queute.enums.forum.VoteType;
import tn.cloudnine.queute.model.forum.Comment;

import java.util.ArrayList;
import java.util.List;
@Data
public class CommentDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private String author;
    private SentimentType sentiment;
    private List<VoteDTO> votes;
    public CommentDTO(Comment comment){
        this.id=comment.getId();
        this.postId=comment.getPost().getId();
        this.userId=comment.getUser().getUser_id();
        this.content=comment.getContent();
        this.author=comment.getUser().getFirs_name();
        sentiment=comment.getSentimentType();
        this.votes=new ArrayList<>();
        if(comment.getVotes() != null){
            comment.getVotes().forEach(c->this.votes.add(new VoteDTO(c)));
        }
    }
}
