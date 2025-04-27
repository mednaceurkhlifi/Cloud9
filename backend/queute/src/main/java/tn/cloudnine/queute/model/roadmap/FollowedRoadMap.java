package tn.cloudnine.queute.model.roadmap;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.cloudnine.queute.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FollowedRoadMap {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id ;
    private String frTitle;
    private String frDescription;
    @ManyToOne
    private User user;
    private Long roadMapId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StepProgress>stepProgressList;
}
