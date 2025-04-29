package tn.cloudnine.queute.utils;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import tn.cloudnine.queute.enums.EmailTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Component
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    /**
     * Send meet invitation
     */
    @Override
    public void sendMeetingMailInvite(
            String to,
            String user_name,
            String meet_admin,
            String date,
            String time,
            String title,
            String meeting_link
    ) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("greeting", user_name);
        properties.put("meeting_date",date);
        properties.put("meeting_time",time);
        properties.put("meeting_subject",title);
        String meeting_msg = "Vous êtes cordialement invité à participer à une réunion importante organisée par " + meet_admin;
        properties.put("meeting_msg", meeting_msg);
        properties.put("meeting_link", meeting_link);

        try {
            sendMail(properties, to, "Meet invitation", EmailTemplate.INVITE_MEETING.getTemplatePath());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Send task deadline notification
     */
    @Override
    public void sendTaskDeadlineNotif(
            String to,
            String user_name,
            String task_title,
            String task_deadline,
            String task_link
    ) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("user_name", user_name);
        properties.put("task_title",task_title);
        properties.put("task_deadline",task_deadline);
        properties.put("task_link",task_link);

        try {
            sendMail(properties, to, "Rappel tâche", EmailTemplate.TASK_NOTIF.getTemplatePath());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMeetingReminder(String email, String fullName, String meetAdmin, String title, String formattedDate, String formattedTime, String meetingLink) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("greeting", fullName);
        properties.put("meeting_date",formattedDate);
        properties.put("meeting_time",formattedTime);
        properties.put("meeting_subject",title);
        String meeting_msg = "Vous avez une réunion prévue dans 5 minutes, organisée par " + meetAdmin + ". Veuillez vous préparer.";
        properties.put("meeting_msg", meeting_msg);
        properties.put("meeting_link", meetingLink);

        try {
            sendMail(properties, email, "Rappel Meeting", EmailTemplate.INVITE_MEETING.getTemplatePath());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Send mail
     */
    private void sendMail(
            Map<String, Object> properties,
            String to,
            String subject,
            String templatePath
    ) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );

        Context context = new Context();
        context.setVariables(properties);

        helper.setTo(to);
        helper.setSubject(subject);

        String emailContent = templateEngine.process(templatePath, context);
        helper.setText(emailContent, true);

        mailSender.send(mimeMessage);
    }


    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}




