package tn.cloudnine.queute.model.workspace;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.enums.workspace.ProjectStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE projects p SET p.isDeleted = true WHERE p.projectId=? AND p.isDeleted = false ")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    private String name;
    private String description;
    private String image;
    private Integer priority;
    private LocalDateTime beginDate;
    private LocalDateTime deadline;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "project_id")
    private Set<ProjectModule> modules;

    @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "project_id")
    private Set<Task> tasks;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
    private boolean isDeleted;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;
}
