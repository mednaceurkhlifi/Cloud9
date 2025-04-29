package tn.cloudnine.queute.model.newsAndOffers.news;

import jakarta.persistence.*;
import lombok.*;
import tn.cloudnine.queute.enums.newsAndOffers.ArticleCategory;
import tn.cloudnine.queute.enums.newsAndOffers.NewsStatus;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.Organization;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;
    private String title;

    private String articleCategory;
    @Column(columnDefinition = "json")
    private String articleType;
    @Column(columnDefinition = "json")
    private String content;
    @ManyToOne
    @JoinColumn(name = "organisationId")
    private Organization organisation;
    @Enumerated(EnumType.STRING)
    private NewsStatus status;
    private String image;

    @Override
    public String toString() {
        return "News{" +
                "newsId=" + newsId +
                ", title='" + title + '\'' +
                ", articleCategory=" + articleCategory +
                ", articleType=" + articleType +
                ", content='" + content + '\'' +
                ", organisation=" + organisation + '\''+
                ", status=" + status +
                '}';
    }
}
