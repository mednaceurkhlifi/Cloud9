package tn.cloudnine.queute.model.workspace;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.enums.workspace.TaskStatus;
import tn.cloudnine.queute.model.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer priority;

    @Column(name = "begin_date")
    private LocalDateTime beginDate;
    private LocalDateTime deadline;

    @ManyToOne
    private Project project;

    @ManyToOne
    private ProjectModule module;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<ProjectDocument> documents;

    @ManyToMany
    private Set<User> members;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public String getFormattedDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return deadline.format(dateFormatter) + " à " + deadline.format(timeFormatter);
    }
}
