package tn.cloudnine.queute.dto.forum;

import lombok.Data;
import tn.cloudnine.queute.enums.forum.SentimentType;
import tn.cloudnine.queute.enums.forum.VoteType;
import tn.cloudnine.queute.model.forum.Comment;

import java.util.ArrayList;
import java.util.Date;
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
    private Date date;
    public CommentDTO(Comment comment){
        this.id=comment.getId();
        this.postId=comment.getPost().getId();
        this.userId=comment.getUser().getUserId();
        this.content=comment.getContent();
        this.author=comment.getUser().getFullName();
        sentiment=comment.getSentimentType();
        this.votes=new ArrayList<>();
        this.date=comment.getDate();
        if(comment.getVotes() != null){
            comment.getVotes().forEach(c->this.votes.add(new VoteDTO(c)));
        }
    }
}
