package tn.cloudnine.queute.repository.news;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.cloudnine.queute.enums.newsAndOffers.SavedStatus;
import tn.cloudnine.queute.model.newsAndOffers.news.ReadLater;
import tn.cloudnine.queute.model.user.User;

import java.util.List;

public interface IReadLaterRepository extends JpaRepository<ReadLater, Long> {

public List<ReadLater>findAllByUser_UserIdAndStatus(Long idUser,SavedStatus status);


    ReadLater findByNews_NewsIdAndUser_UserId(Long newsNewsId, Long userUserId);

    void deleteByUser_UserIdAndNews_NewsId(Long userUserId, Long newsNewsId);
}
