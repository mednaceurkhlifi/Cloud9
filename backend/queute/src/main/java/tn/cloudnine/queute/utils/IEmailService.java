package tn.cloudnine.queute.utils;

import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendMeetingMailInvite(
            String to,
            String user_name,
            String meet_admin,
            String date,
            String time,
            String title,
            String meeting_link
    );

    void sendTaskDeadlineNotif(
            String to,
            String user_name,
            String task_title,
            String task_deadline,
            String task_link
    );

    void sendMeetingReminder(String email, String fullName, String meetAdmin, String title, String formattedDate, String formattedTime, String meetingLink);
}
