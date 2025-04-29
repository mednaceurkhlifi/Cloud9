package tn.cloudnine.queute.model.ServiceAndFeedback.feedback.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedBackDTO {
    private Long id;
    private double note;
    private String comment;
    private boolean isRead;
}
