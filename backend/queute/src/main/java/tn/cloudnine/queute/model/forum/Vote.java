package tn.cloudnine.queute.model.forum;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import tn.cloudnine.queute.enums.forum.VoteType;
import tn.cloudnine.queute.model.user.User;

@Data
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private VoteType voteType;
    @ManyToOne
    private Votable votable;
    @ManyToOne
    private User user;
}
