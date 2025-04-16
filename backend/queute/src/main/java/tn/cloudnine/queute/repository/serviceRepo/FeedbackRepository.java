package tn.cloudnine.queute.repository.serviceRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.ServiceAndFeedback.feedback.Feedback;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
    @Query("SELECT Feedback FROM Feedback f where f.service.id=: serviceId")
    List<Feedback> findAllByServiceId(Long serviceId);
}
