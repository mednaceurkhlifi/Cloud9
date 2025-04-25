package tn.cloudnine.queute.repository.news;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.cloudnine.queute.model.newsAndOffers.news.Reaction;

import java.util.List;

public interface IReactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findReactionByUser_UserId(Long userUserId);

    Reaction findReactionByUser_UserIdAndNews_NewsId(Long userUserId, Long newsNewsId);

    List<Reaction> findAllByNews_NewsId(Long newsNewsId);
}