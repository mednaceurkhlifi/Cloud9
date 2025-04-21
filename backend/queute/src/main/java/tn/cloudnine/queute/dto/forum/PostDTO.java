package tn.cloudnine.queute.dto.forum;

import lombok.Data;
import tn.cloudnine.queute.enums.forum.SentimentType;
import tn.cloudnine.queute.model.forum.ImageEntity;
import tn.cloudnine.queute.model.forum.Post;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostDTO {
    protected Long id;
    private String Title;
    private String content;
    private Long userId;
    private List<CommentDTO> comments;
    private List<VoteDTO> votes;
    private ImageEntity image;
    private SentimentType sentiment;
    public PostDTO(Post post) {
        this.id = post.getId();
        this.Title = post.getTitle();
        this.content = post.getContent();
        this.userId=post.getUser().getUser_id();
        sentiment=post.getSentimentType();
        if(post.getImage() != null) {
            this.image=post.getImage();
        }
        this.comments=new ArrayList<>();
        if(post.getComments() != null){
            post.getComments().forEach(c->this.comments.add(new CommentDTO(c)));
        }
        this.votes=new ArrayList<>();
        if(post.getVotes() !=null){
            post.getVotes().forEach(c->this.votes.add(new VoteDTO(c)));
        }
    }
}
