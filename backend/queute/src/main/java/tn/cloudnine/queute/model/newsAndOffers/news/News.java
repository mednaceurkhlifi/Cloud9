package tn.cloudnine.queute.model.newsAndOffers.news;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.cloudnine.queute.enums.newsAndOffers.ArticleCategory;
import tn.cloudnine.queute.enums.newsAndOffers.ArticleType;
import tn.cloudnine.queute.model.newsAndOffers.offers.Offers;
import tn.cloudnine.queute.model.organization.Organization;

import java.io.File;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsNum;
    private String name;
   private String description;
   private String attachedFile;
    @Enumerated(EnumType.STRING)
   private ArticleCategory articleCategory;
    @Enumerated(EnumType.STRING)
    private ArticleType articleType;
    @OneToMany
    private List <Offers> offers;




}
