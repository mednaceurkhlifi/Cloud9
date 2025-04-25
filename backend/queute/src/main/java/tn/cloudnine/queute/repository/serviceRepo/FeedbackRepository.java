package tn.cloudnine.queute.repository.serviceRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.ServiceAndFeedback.feedback.Feedback;
import tn.cloudnine.queute.model.user.User;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByOrganisationId(Long organisationId);


    @Query("SELECT AVG(f.note) FROM Feedback f WHERE f.organisation.id = :organizationId")
    Double findAverageRateByOrganizationId(Long organizationId);


    List<Feedback> findFeedbacksByUser(User user);

}
