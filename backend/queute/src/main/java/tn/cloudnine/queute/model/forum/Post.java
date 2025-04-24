package tn.cloudnine.queute.model.forum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Auditable;
import tn.cloudnine.queute.enums.forum.VoteType;
import tn.cloudnine.queute.model.user.User;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Post extends Votable {
    private String title;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id",nullable = true)
    private ImageEntity image;
    @OneToMany(mappedBy = "votable", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Vote> votes;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public int getUpvotes() {
        long ret= votes.stream().filter(v-> v.getVoteType() == VoteType.UPVOTE).count()    ;
        return (int) ret;
    }
}
