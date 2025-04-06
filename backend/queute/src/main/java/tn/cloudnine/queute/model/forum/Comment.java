package tn.cloudnine.queute.model.forum;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import tn.cloudnine.queute.model.user.User;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Comment extends Votable {

    @OneToMany(mappedBy = "votable", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Vote> votes;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference
    private Post post;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
