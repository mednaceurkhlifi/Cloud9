package tn.cloudnine.queute.model.ServiceAndFeedback.feedback;
import jakarta.persistence.*;


import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.Organization;
import tn.cloudnine.queute.model.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name = "feedback")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE feedback f SET f.is_deleted = true WHERE f.feedback_id=? AND f.is_deleted = false ")
public class Feedback implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double note;
    private String comment;

    @Column(name = "is_read")
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "organisation_id", nullable = false)

    private Organization organisation;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

}
