package tn.cloudnine.queute.model.workspace;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.model.user.User;

@Entity
@Table(name = "message_status")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MessageStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageStatusId;
    private boolean seen;
    @ManyToOne
    private User member;
}
