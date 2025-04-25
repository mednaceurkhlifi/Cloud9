package tn.cloudnine.queute.model.newsAndOffers.news;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.cloudnine.queute.model.user.User;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trending implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trendingId;
    private LocalDate date;
    private String type;
    private int score;
    private String active;//nchouf fiha idha active walla la nhot fi journalisation idha naha el react walla momken 9dima
    @ManyToOne
    @JoinColumn(name = "newsId")
    private News news;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Override
    public String toString() {
        return "Trending{" +
                "trendingId=" + trendingId +
                ", date=" + date +
                ", news=" + news +
                ", user=" + user +
                '}';
    }
}
