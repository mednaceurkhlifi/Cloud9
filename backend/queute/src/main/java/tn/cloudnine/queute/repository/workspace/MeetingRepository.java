package tn.cloudnine.queute.repository.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.dto.workspace.projections.MeetingProjection;
import tn.cloudnine.queute.model.workspace.Meeting;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<MeetingProjection> findByMeetingId(Long meeting_id);

    @Query("SELECT m FROM Meeting m JOIN m.members u WHERE u.email = :user_email")
    Page<MeetingProjection> getInvitedMeetings(@Param("user_email") String user_email, Pageable pageable);

    Page<MeetingProjection> findByAdmin_Email(String email, Pageable pageable);
}
