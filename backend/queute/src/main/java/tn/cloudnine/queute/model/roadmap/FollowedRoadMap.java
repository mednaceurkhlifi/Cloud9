package tn.cloudnine.queute.model.roadmap;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private boolean isUpdated;
    private String info;
    private LocalDateTime followDate;
    @OneToOne
    private RoadMap roadMap;
    @OneToMany
    private List<StepProgress>stepProgressList;
}
