package tn.cloudnine.queute.model.workspace;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.model.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "workspace_messages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WorkspaceMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JsonIgnore
    private User sender;
    @Column(columnDefinition = "TEXT")
    private String message;

    @ManyToOne
    private Workspace workspace;
    @ManyToOne
    private Project project;
    @ManyToOne
    private ProjectModule module;
    @ManyToOne
    private Task task;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ProjectDocument> attachments;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
