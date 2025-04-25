package tn.cloudnine.queute.model.workspace;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.enums.workspace.ProjectRole;
import tn.cloudnine.queute.model.Embeddable.ProjectUserId;
import tn.cloudnine.queute.model.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProjectUser {

    @EmbeddedId
    private ProjectUserId id;

    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    @ManyToOne
    @MapsId("project")
    private Project project;

    @ManyToOne
    @MapsId("user")
    private User user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;
}
