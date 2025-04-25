package tn.cloudnine.queute.model.newsAndOffers.news;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.cloudnine.queute.enums.newsAndOffers.SavedStatus;
import tn.cloudnine.queute.model.user.User;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadLater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long readLaterId;
    @ManyToOne
    @JoinColumn(name = "newsId")
    private News news;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @Enumerated(EnumType.STRING)

    private SavedStatus status;

    @Override
    public String toString() {
        return "ReadLater{" +
                "readLaterId=" + readLaterId +
                ", news=" + news +
                ", user=" + user +
                ", status=" + status +
                '}';
    }
}
