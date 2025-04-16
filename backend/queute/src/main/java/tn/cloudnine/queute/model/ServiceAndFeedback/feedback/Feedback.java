package tn.cloudnine.queute.model.ServiceAndFeedback.feedback;
import jakarta.persistence.*;


import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.model.ServiceAndFeedback.services.Services;
import tn.cloudnine.queute.model.user.User;

import java.io.Serializable;
import java.security.cert.CertPathBuilder;
import java.time.LocalDateTime;
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)

public class Feedback implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String content;
    //private String status;

     private Double note;
     private String comment;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;


    @LastModifiedBy
    @Column(insertable = false)
    private  Long lastModifiedBy;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services service; // Relation avec Service

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User user;  // Instead of Integer



}
