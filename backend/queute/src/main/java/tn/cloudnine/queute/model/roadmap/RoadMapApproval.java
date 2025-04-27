package tn.cloudnine.queute.model.roadmap;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import tn.cloudnine.queute.model.user.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoadMapApproval {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id ;
    private boolean isApproved;
    private LocalDateTime date;
    @ManyToOne
    private User user;

    @Override
    public String toString() {
        return "RoadMapApproval{" +
                "id=" + id +
                ", isApproved=" + isApproved +
                ", date=" + date +
                ", user=" + user +
                '}';
    }
}
