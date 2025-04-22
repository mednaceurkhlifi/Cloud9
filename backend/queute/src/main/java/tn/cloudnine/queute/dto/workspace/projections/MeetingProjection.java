package tn.cloudnine.queute.dto.workspace.projections;

import java.time.LocalDateTime;
import java.util.Set;

public interface MeetingProjection {

    Long getMeetingId();
    String getTitle();
    String getDescription();
    Set<UserProjection> getMembers();
    UserProjection getAdmin();
    LocalDateTime getBeginDate();
    LocalDateTime getEndDate();
    LocalDateTime getCreatedAt();
}
