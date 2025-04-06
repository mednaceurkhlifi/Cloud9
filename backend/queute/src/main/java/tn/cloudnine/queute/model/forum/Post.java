package tn.cloudnine.queute.model.forum;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Auditable;
import tn.cloudnine.queute.model.user.User;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Post extends Votable {
    private String title;
    @OneToOne
    @JoinColumn(name = "image_id",nullable = true)
    private ImageEntity image;
    @OneToMany(mappedBy = "votable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
