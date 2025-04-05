package tn.cloudnine.queute.model.workspace;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.enums.workspace.ProjectRole;
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
@SQLDelete(sql = "UPDATE project_users pu SET pu.is_deleted = true WHERE pu.project_user_id=? AND pu.is_deleted = false ")
public class ProjectUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_user_id;

    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    @ManyToOne
    private User member;
    @ManyToOne
    private Project project;

    private boolean is_deleted;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;
}
