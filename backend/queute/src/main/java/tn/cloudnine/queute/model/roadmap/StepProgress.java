package tn.cloudnine.queute.model.roadmap;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StepProgress {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id ;
    private boolean isDone;
    private String spDescription;
    private String spRequiredPaper ;
    private Long stepId ;
}
