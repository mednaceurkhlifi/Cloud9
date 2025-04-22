package tn.cloudnine.queute.service.workspace;

import tn.cloudnine.queute.dto.workspace.projections.MeetingProjection;
import tn.cloudnine.queute.dto.workspace.responses.MeetingResponse;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.model.workspace.Meeting;

import java.util.Set;

public interface IMeetingService {

    MeetingProjection createNewMeeting(Meeting meeting);

    void deleteMeeting(Long meetingId);

    MeetingProjection inviteToMeeting(Long meetingId, Set<User> invited);

    MeetingProjection getMeetingById(Long meetingId);

    MeetingResponse getInvitedMeetings(String userEmail, Integer size, Integer page_no);

    MeetingResponse getUserMeetings(String userEmail, Integer size, Integer page_no);
}
