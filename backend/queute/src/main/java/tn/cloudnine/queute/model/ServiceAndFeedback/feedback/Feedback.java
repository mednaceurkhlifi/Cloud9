package tn.cloudnine.queute.model.ServiceAndFeedback;
import jakarta.persistence.*;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.cloudnine.queute.model.ServiceAndFeedback.services.Service;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String status;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service; // Relation avec Service
}
