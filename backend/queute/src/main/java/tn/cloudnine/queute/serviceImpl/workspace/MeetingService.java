package tn.cloudnine.queute.serviceImpl.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.dto.workspace.projections.MeetingProjection;
import tn.cloudnine.queute.dto.workspace.responses.MeetingResponse;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.model.workspace.Meeting;
import tn.cloudnine.queute.repository.user.UserRepository;
import tn.cloudnine.queute.repository.workspace.MeetingRepository;
import tn.cloudnine.queute.service.workspace.IMeetingService;
import tn.cloudnine.queute.utils.IEmailService;
import tn.cloudnine.queute.utils.IMeetingReminderScheduler;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MeetingService implements IMeetingService {

    private final MeetingRepository repository;
    private final UserRepository userRepository;
    private final IEmailService emailService;
    private final IMeetingReminderScheduler meetingReminderScheduler;
    private final String BASE_LINK = "http://localhost:4200/meeting/";

    @Override
    public MeetingProjection createNewMeeting(Meeting meeting) {
        User admin = userRepository.findByEmailEquals(meeting.getAdmin().getEmail()).orElseThrow(
                () -> new IllegalArgumentException("User not found.")
        );
        meeting.setAdmin(admin);
        Set<User> members = new HashSet<>();
        if(!meeting.getMembers().isEmpty()) {
            for (User member : meeting.getMembers()) {
                member = userRepository.findByEmailEquals(member.getEmail()).orElseThrow(
                        () -> new IllegalArgumentException("User not found.")
                );
                members.add(member);
            }
        }
        meeting.setMembers(members);
        meeting = repository.save(meeting);
        sendInvitation(meeting);
        meetingReminderScheduler.scheduleMeetingReminder(meeting);
        return repository.findByMeetingId(meeting.getMeetingId()).orElseThrow(
                () -> new IllegalArgumentException("Meeting not found.")
        );
    }

    @Override
    public void deleteMeeting(Long meetingId) {
        Meeting meeting = repository.findById(meetingId).orElseThrow(
                () -> new IllegalArgumentException("Meeting not found.")
        );
        repository.delete(meeting);
    }

    @Override
    public MeetingProjection inviteToMeeting(Long meetingId, Set<User> invited) {
        Meeting meeting = repository.findById(meetingId).orElseThrow(
                () -> new IllegalArgumentException("Meeting not found.")
        );
        for (User user : invited) {
            user = userRepository.findByEmailEquals(user.getEmail()).orElseThrow(
                    () -> new IllegalArgumentException("User not found.")
            );
            String link = BASE_LINK + meeting.getMeetingId() + "/" + user.getUserId() + "/" + user.getFull_name();
            emailService.sendMeetingMailInvite(
                    user.getEmail(), user.getFull_name(),
                    meeting.getAdmin().getFull_name(),
                    meeting.getFormattedDate(),
                    meeting.getFormattedTime(), meeting.getTitle(),link
            );
            meeting.getMembers().add(user);
        }
        repository.save(meeting);
        return repository.findByMeetingId(meeting.getMeetingId()).orElseThrow(
                () -> new IllegalArgumentException("Meeting not found.")
        );
    }

    @Override
    public MeetingProjection getMeetingById(Long meetingId) {
        return repository.findByMeetingId(meetingId).orElseThrow(
                () -> new IllegalArgumentException("Meeting not found.")
        );
    }

    @Override
    public MeetingResponse getInvitedMeetings(String userEmail, Integer size, Integer page_no) {
        Pageable pageable = PageRequest.of(page_no, size);
        Page<MeetingProjection> meetings = repository.getInvitedMeetings(userEmail, pageable);

        return new MeetingResponse(
                meetings.toList(), meetings.getNumber(),
                meetings.getSize(), meetings.getTotalElements(),
                meetings.getTotalPages(), meetings.isLast()
        );
    }

    @Override
    public MeetingResponse getUserMeetings(String userEmail, Integer size, Integer page_no) {
        Pageable pageable = PageRequest.of(page_no, size);
        Page<MeetingProjection> meetings = repository.findByAdmin_Email(userEmail, pageable);

        return new MeetingResponse(
                meetings.toList(), meetings.getNumber(),
                meetings.getSize(), meetings.getTotalElements(),
                meetings.getTotalPages(), meetings.isLast()
        );
    }

    /**
     * Util methods
     */
    private void sendInvitation(Meeting meeting) {
        for (User user : meeting.getMembers()) {
            String link = BASE_LINK + meeting.getMeetingId() + "/" + user.getUserId() + "/" + user.getFull_name();
            emailService.sendMeetingMailInvite(
                    user.getEmail(), user.getFull_name(),
                    meeting.getAdmin().getFull_name(),
                    meeting.getFormattedDate(),
                    meeting.getFormattedTime(), meeting.getTitle(),link
            );
        }
    }
}
