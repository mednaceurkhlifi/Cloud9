package tn.cloudnine.queute.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tn.cloudnine.queute.model.ServiceAndFeedback.feedback.DTO.FeedBackDTO;
import tn.cloudnine.queute.model.ServiceAndFeedback.feedback.Feedback;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.Organization;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.repository.serviceRepo.FeedbackRepository;
import tn.cloudnine.queute.repository.serviceRepo.OrganizationRepository;
import tn.cloudnine.queute.repository.user.UserRepository; // Import correct
import tn.cloudnine.queute.utils.EmailService;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Feedback addFeedbackToOrganization(Long organisationId, Feedback feedback) {
        // Récupérer l'organisation
        Organization organisation = organizationRepository.findById(organisationId)
                .orElseThrow(() -> new RuntimeException("Organization not found with ID: " + organisationId));

        User staticUser = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Static user not found with ID: 1"));

        boolean alreadyExists = feedbackRepository.existsByUser_UserIdAndOrganisation_OrganizationId(staticUser.getUserId(), organisationId);
        if (alreadyExists) {
            throw new RuntimeException("User has already submitted feedback for this organization.");
        }

        feedback.setUser(staticUser);
        feedback.setOrganisation(organisation);
        feedback.setRead(false);

        Feedback savedFeedback = feedbackRepository.save(feedback);

        // Mettre à jour la moyenne
        Double avgRate = feedbackRepository.findAverageRateByOrganizationId(organisationId);
        organisation.setAverageRate(avgRate != null ? avgRate.floatValue() : 0f);
        organizationRepository.save(organisation);

        // Envoyer la notification
        String userName = staticUser.getFull_name();
        String message = feedback.getComment();
        String notification = "Nouvelle feedback de " + userName + " : " + message;
        messagingTemplate.convertAndSend("/topic/notifications", notification);

        return savedFeedback;
    }


    public List<Feedback> getFeedbacksByOrganization(Long organisationId) {
        return feedbackRepository.findByOrganisation_OrganizationId(organisationId);
    }

    public Feedback markFeedbackAsRead(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + feedbackId));
        feedback.setRead(true);
        return feedbackRepository.save(feedback);
    }

    public Double getAverageRateByOrganizationId(Long organizationId) {
        Double averageRate = feedbackRepository.findAverageRateByOrganizationId(organizationId);
        if (averageRate != null) {
            return Math.round(averageRate * 10.0) / 10.0; // Arrondi à 1 décimale
        }
        return null; // Retourne null si aucune note n'est trouvée
    }
    public List<FeedBackDTO> getFeedbacksForStaticUser() {
        Long userId = 1L;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return feedbackRepository.findFeedbacksByUser(user).stream()
                .map(f -> new FeedBackDTO(f.getFeedbackId(), f.getNote(), f.getComment(),f.isRead()))
                .collect(Collectors.toList());
    }


    public void respondToFeedbackByEmail(Long feedbackId, String responseBody) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback introuvable avec l'ID : " + feedbackId));

        User user = feedback.getUser();
        if (user == null || user.getEmail() == null) {
            throw new RuntimeException("Utilisateur ou email introuvable pour ce feedback.");
        }


        String userEmail = user.getEmail();
        String subject = "Réponse à votre feedback";
        emailService.sendSimpleEmail(userEmail, subject, responseBody);
        feedback.setRead(true);
        feedbackRepository.save(feedback);
    }



}
