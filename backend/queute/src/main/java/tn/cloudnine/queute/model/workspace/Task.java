package tn.cloudnine.queute.model.workspace;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.enums.workspace.TaskStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tasks t SET t.isDeleted = true WHERE t.taskId=? AND t.isDeleted = false ")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    private String title;
    private String description;
    private Integer priority;
    private LocalDateTime beginDate;
    private LocalDateTime deadline;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<ProjectDocument> documents;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private boolean isDeleted;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
