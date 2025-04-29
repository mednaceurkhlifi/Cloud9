package tn.cloudnine.queute.controllers.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.model.ServiceAndFeedback.feedback.DTO.FeedBackDTO;
import tn.cloudnine.queute.model.ServiceAndFeedback.feedback.Feedback;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.repository.user.UserRepository;
import tn.cloudnine.queute.serviceImpl.FeedbackService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedbacks")
@CrossOrigin(origins = "http://localhost:4200")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;


    // 🔹 Add feedback to an organization
    @PostMapping("/organization/{organisationId}")
    public ResponseEntity<Feedback> addFeedbackToOrganization(
            @PathVariable Long organisationId,
            @RequestBody Feedback feedback) {
        Feedback createdFeedback = feedbackService.addFeedbackToOrganization(organisationId, feedback);
        return ResponseEntity.status(201).body(createdFeedback);
    }

    // 🔹 Get all feedbacks for an organization)
    @GetMapping("/organization/{organisationId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByOrganization(@PathVariable Long organisationId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByOrganization(organisationId);
        return ResponseEntity.ok(feedbacks);
    }

    // 🔹 Mark feedback as read
    @PutMapping("/{feedbackId}/mark-as-read")
    public ResponseEntity<Feedback> markFeedbackAsRead(@PathVariable Long feedbackId) {
        Feedback updatedFeedback = feedbackService.markFeedbackAsRead(feedbackId);
        return ResponseEntity.ok(updatedFeedback);
    }

    @GetMapping("/user/feedbacks")
    public ResponseEntity<List<FeedBackDTO>> getFeedbacks() {
        List<FeedBackDTO> feedbacks = feedbackService.getFeedbacksForStaticUser();
        return ResponseEntity.ok(feedbacks);
    }


    @PostMapping("/feedbacks/respond/{feedbackId}")
    public ResponseEntity<Map<String, String>> respondToFeedback(@PathVariable Long feedbackId, @RequestBody String responseBody) {
        feedbackService.respondToFeedbackByEmail(feedbackId, responseBody);

        // Créer une réponse JSON sous forme de Map
        Map<String, String> response = new HashMap<>();
        response.put("message", "Réponse envoyée avec succès par email.");

        return ResponseEntity.ok(response);
    }

}
