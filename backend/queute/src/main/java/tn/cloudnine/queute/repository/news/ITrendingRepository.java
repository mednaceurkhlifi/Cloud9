package tn.cloudnine.queute.repository.news;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.cloudnine.queute.model.newsAndOffers.news.Trending;

import java.time.LocalDate;
import java.util.List;

public interface ITrendingRepository extends JpaRepository<Trending, Long> {


    Trending findByUser_UserIdAndNews_NewsIdAndTypeAndDateBetween(Long userUserId, Long newsNewsId, String type, LocalDate dateAfter, LocalDate dateBefore);

    @Transactional
    @Modifying
    @Query("UPDATE Trending t SET t.active = 'false' WHERE t.news.newsId = :newsId AND t.user.userId = :userId AND t.type <> 'visit' and t.active = 'true'")
    int changeActiveState(@Param("newsId") Long newsId, @Param("userId") Long userId, @Param("type") String type);

    @Query("SELECT t.type from Trending t where t.news.newsId = :newsId AND t.user.userId = :userId AND t.type<>'visit' AND t.active='true'")
    String  getType(@Param("newsId") Long newsId, @Param("userId") Long userId);

    @Query("SELECT t.news ,sum(t.score) FROM Trending t WHERE t.active='true' AND t.date BETWEEN :startDate AND :endDate GROUP BY t.news order by sum(t.score) desc ")
    List<Object[]> countScoresGroupedByNews(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


}