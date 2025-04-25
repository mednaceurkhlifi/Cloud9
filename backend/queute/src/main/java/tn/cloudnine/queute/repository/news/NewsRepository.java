package tn.cloudnine.queute.repository.news;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.cloudnine.queute.enums.newsAndOffers.ArticleCategory;
import tn.cloudnine.queute.model.newsAndOffers.news.News;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
   // List<News> findAllByOrganisation_OrgId(Long organisationOrgId);

    List<News> findByNewsIdAndArticleCategory(Long newsId, ArticleCategory articleCategory);

    @Query("SELECT count (*) from News n where n.organisation.organizationId= :orgId ")
    Long getNumberNews(@Param("orgId") Long orgId);

    @Query("SELECT COUNT(*), FUNCTION('MONTH', t.date), FUNCTION('YEAR', t.date) " +
            "FROM Trending t JOIN t.news n " +
            "WHERE n.organisation.organizationId = :orgId AND t.active = 'true' " +
            "GROUP BY FUNCTION('MONTH', t.date), FUNCTION('YEAR', t.date) " +
            "ORDER BY FUNCTION('MONTH', t.date), FUNCTION('YEAR', t.date)")
    List<Object[]> getNumberOfActions(@Param("orgId") Long orgId);


    List<News> findAllByOrganisation_OrganizationId(Long organisationOrganizationId);
}