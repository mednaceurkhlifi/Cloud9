package tn.cloudnine.queute.model.newsAndOffers.news;

import jakarta.persistence.*;
import lombok.*;
import tn.cloudnine.queute.enums.newsAndOffers.ReactionType;
import tn.cloudnine.queute.model.user.User;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
    private LocalDate reactionDate;



    @ManyToOne
    @JoinColumn(name = "userId")
    User user;
    @ManyToOne
    @JoinColumn(name = "newsId")
    News news;

    @Override
    public String toString() {
        return "Reaction{" +
                "reactionId=" + reactionId +
                ", reactionType=" + reactionType +
                ", reactionDate=" + reactionDate +
                ", user=" + user +
                ", news=" + news +
                '}';
    }
}
