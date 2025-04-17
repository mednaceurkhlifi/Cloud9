package tn.cloudnine.queute.dto.forum;

import lombok.Data;
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
    PostDTO(Post post) {
        this.id = post.getId();
        this.Title = post.getTitle();
        this.content = post.getContent();
        this.userId=post.getUser().getUser_id();
        this.comments=new ArrayList<>();
        post.getComments().forEach(c->this.comments.add(new CommentDTO(c)));
        this.votes=new ArrayList<>();
        post.getVotes().forEach(c->this.votes.add(new VoteDTO(c)));
    }
}
