package tn.cloudnine.queute.model.ServiceAndFeedback.services;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.cloudnine.queute.model.ServiceAndFeedback.feedback.Feedback;
import tn.cloudnine.queute.model.ServiceAndFeedback.office.Office;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private String type;
    private String description;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks; // Relation avec Feedback

    @ManyToOne
    private Office office; // Relation avec Office
}
