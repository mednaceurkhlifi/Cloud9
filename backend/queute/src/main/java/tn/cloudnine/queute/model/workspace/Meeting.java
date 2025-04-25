package tn.cloudnine.queute.model.workspace;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.model.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "meetings")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Meeting implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<User> members;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private User admin;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public String getFormattedDate() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return beginDate.format(dateFormatter);
    }

    public String getFormattedTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return beginDate.format(timeFormatter);
    }
}
