package tn.cloudnine.queute.model.ServiceAndFeedback.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;
import tn.cloudnine.queute.model.ServiceAndFeedback.feedback.Feedback;
import tn.cloudnine.queute.model.ServiceAndFeedback.office.Office;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@EntityListeners(AuditingEntityListener.class)
@Builder

public class Services implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;


    @Column()
    private String serviceName;
    @Column()
    private String type;
    @Column()
    private String description;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;


    @LastModifiedBy
    @Column(insertable = false)
    private  Integer lastModifiedBy;
    @JsonIgnore
    @ManyToOne
    private Office office; // Relation avec Office


}
