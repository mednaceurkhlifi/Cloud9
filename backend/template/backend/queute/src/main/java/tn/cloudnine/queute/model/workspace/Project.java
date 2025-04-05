package tn.cloudnine.queute.model.workspace;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.enums.workspace.ProjectStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE projects p SET p.is_deleted = true WHERE p.project_id=? AND p.is_deleted = false ")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_id;
    private String name;
    private String description;
    private String image;
    private Integer priority;
    private LocalDateTime begin_date;
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private boolean is_deleted;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;
}
