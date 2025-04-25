package tn.cloudnine.queute.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.model.workspace.Meeting;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class MeetingReminderScheduler implements IMeetingReminderScheduler {

    private final TaskScheduler taskScheduler;
    private final IEmailService emailService;
    private final String BASE_LINK = "http://localhost:4200/meeting/";

    public void scheduleMeetingReminder(Meeting meeting) {
        LocalDateTime reminderTime = meeting.getBeginDate().minusMinutes(5);
        Date executionTime = Date.from(reminderTime.atZone(ZoneId.systemDefault()).toInstant());

        taskScheduler.schedule(() -> {
            for (User user : meeting.getMembers()) {
                String meet_admin = "vous";
                String meeting_link = BASE_LINK + meeting.getMeetingId() + "/" + user.getUserId() + "/" + user.getFullName();
                if(!meeting.getAdmin().getEmail().equals(user.getEmail())) meet_admin = meeting.getAdmin().getFullName();
                emailService.sendMeetingReminder(
                        user.getEmail(),
                        user.getFullName(),
                        meet_admin,
                        meeting.getTitle(),
                        meeting.getFormattedDate(),
                        meeting.getFormattedTime(),
                        meeting_link
                );
            }
        }, executionTime);
    }
}
